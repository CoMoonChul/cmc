package com.sw.cmc.adapter.in.lcd.web;

import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.domain.lcd.LiveCodingAction;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
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

    public WebSocketControllerImpl(LiveCodingUseCase liveCodingUseCase ) {
        this.liveCodingUseCase = liveCodingUseCase;
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
            throw new CmcException("LCD001");
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

        rooms.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        rooms.get(roomId).add(session);

        System.out.println("✅ WebSocket 연결됨: " + roomId + " (세션 수: " + rooms.get(roomId).size() + ")");
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        Long userNum = (Long) session.getAttributes().get("userNum");
        if (userNum == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
            throw new CmcException("LCD013");
        }

        String roomId = getRoomId(session);
        if (roomId.isEmpty()) {
            throw new CmcException("LCD001");
        }

        LiveCodingDomain roomInfo = liveCodingUseCase.selectLiveCoding(UUID.fromString(roomId));
        Set<WebSocketSession> sessions = rooms.getOrDefault(roomId, Collections.emptySet());

        boolean isHost = userNum.equals(roomInfo.getHostId());

        if (isHost) {
            // 호스트가 나가면 모든 참가자 세션 종료
            for (WebSocketSession s : new HashSet<>(sessions)) {
                try {
                    s.close(CloseStatus.GOING_AWAY);
                } catch (IOException e) {
                    throw new CmcException("LCD014");
                }
            }

            rooms.remove(roomId);
            boolean deleted = liveCodingUseCase.deleteLiveCoding(UUID.fromString(roomId));
            if (!deleted) {
                throw new CmcException("LCD004");
            }

            System.out.println("🚫 방 종료: " + roomId);
        } else {
            // ⬇️ 기존 세션을 바로 제거하지 않고, 일정 시간 유지 (새로고침 복구를 위해)
            sessions.remove(session);

            // 기존 참가자가 재접속할 가능성이 있으므로 일정 시간 동안 대기
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 일정 시간 후에도 같은 유저가 없다면 방에서 제거
                    Set<WebSocketSession> updatedSessions = rooms.getOrDefault(roomId, Collections.emptySet());
                    boolean stillExists = updatedSessions.stream()
                            .anyMatch(s -> s.getAttributes().get("userNum").equals(userNum));

                    if (!stillExists) {
                        try {
                            liveCodingUseCase.updateLiveCoding(roomInfo.getRoomId(), userNum, LiveCodingAction.LEAVE.getAction());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("🛑 세션 유지 시간 초과: " + userNum + " 제거됨");
                    }
                }
            }, 5000); // ⏳ 5초 동안 기존 유저가 다시 접속할 기회를 줌
        }

        System.out.println("❌ WebSocket 연결 종료: " + roomId + " (남은 세션 수: " + rooms.getOrDefault(roomId, Collections.emptySet()).size() + ")");
    }


}