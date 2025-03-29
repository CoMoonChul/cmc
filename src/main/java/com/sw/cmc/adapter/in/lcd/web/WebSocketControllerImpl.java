package com.sw.cmc.adapter.in.lcd.web;

import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.lcd.LiveCodingAction;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * packageName    : com.sw.cmc.adapter.in.lcd
 * fileName       : WebSocketController
 * author         : 82104
 * date           : 2025-02-11
 * description    : 웹소켓 컨트롤러
 */
@Component
public class WebSocketControllerImpl extends TextWebSocketHandler {

    // 방 별로 WebSocket 세션을 관리하는 ConcurrentHashMap
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private final LiveCodingUseCase liveCodingUseCase;

    private final UserUtil userUtil;

    public WebSocketControllerImpl(LiveCodingUseCase liveCodingUseCase, UserUtil userUtil) {
        this.liveCodingUseCase = liveCodingUseCase;
        this.userUtil = userUtil;
    }


    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        String roomId = getRoomId(session);
        if (roomId.isEmpty()) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        String payload = message.getPayload();
        Set<WebSocketSession> roomSessions = rooms.getOrDefault(roomId, Set.of());

        // 같은 방에 있는 모든 유저들에게 메시지 전송
        for (WebSocketSession s : roomSessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(payload));
            }
        }
    }

    // roomId를 올바르게 추출하도록 수정: 인덱스 4 사용
    private String getRoomId(WebSocketSession session) {
        try {
            String[] pathSegments = Objects.requireNonNull(session.getUri()).getPath().split("/");
            if (pathSegments.length >= 4) {
                return pathSegments[3]; // roomId 추출 (인덱스 3 사용)
            }
        } catch (Exception e) {
            System.err.println("⚠️ WebSocket 연결 오류: 올바른 roomId를 찾을 수 없음.");
        }
        return "";
    }

    // open call back
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {

        Long userNum = (Long) session.getAttributes().get("userNum");

        if (userNum == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
            throw new CmcException("LCD013");
        }

        String roomId = getRoomId(session);
        if (roomId.isEmpty()) {
            session.close(CloseStatus.BAD_DATA);
            throw new CmcException("LCD001");
        }

        LiveCodingDomain roomInfo = liveCodingUseCase.selectLiveCoding(UUID.fromString(roomId));

        boolean isHost = userNum.equals(roomInfo.getHostId());
        if (!isHost) {
            liveCodingUseCase.updateLiveCoding(roomInfo.getRoomId(), userNum, LiveCodingAction.JOIN.getAction());
        }
        rooms.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        rooms.get(roomId).add(session);

        System.out.println("✅ WebSocket 연결됨: " + roomId + " (세션 수: " + rooms.get(roomId).size() + ")");
    }

    // close call back
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        System.out.println("#22222222222222222222222222");
        String roomId = getRoomId(session);
        LiveCodingDomain roomInfo = liveCodingUseCase.selectLiveCoding(UUID.fromString(roomId));

        if (roomId.isEmpty()) {
            return;
        }

        Set<WebSocketSession> sessions = rooms.getOrDefault(roomId, Collections.emptySet());
        sessions.remove(session);

        // 방에 더 이상 세션이 없다면 제거
        if (sessions.isEmpty()) {
            rooms.remove(roomId);
            boolean deleted = liveCodingUseCase.deleteLiveCoding(UUID.fromString(roomId));
            if (!deleted) {
                throw new CmcException("LCD004");
            }

        }

        System.out.println("❌ WebSocket 연결 종료: " + roomId + " (남은 세션 수: " + rooms.getOrDefault(roomId, Collections.emptySet()).size() + ")");
    }
}