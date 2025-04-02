package com.sw.cmc.adapter.in.lcd.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.domain.lcd.*;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WebSocketControllerImpl(LiveCodingUseCase liveCodingUseCase ) {
        this.liveCodingUseCase = liveCodingUseCase;
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        Long userNum = (Long) session.getAttributes().get("userNum");

        String roomId = getRoomId(session);
        if (roomId.isEmpty()) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        String payload = message.getPayload();
        System.out.println("#33 message : " );
        System.out.println(message);
        System.out.println("===============");

        Set<WebSocketSession> roomSessions = rooms.getOrDefault(roomId, Set.of());

        // 같은 방에 있는 모든 유저들에게 메시지 전송
        for (WebSocketSession s : roomSessions) {
            if (s.isOpen()) {
                LiveCodingChatDomain liveCodingChatDomain = new LiveCodingChatDomain();
                liveCodingChatDomain.setLiveCodingChatType(LiveCodingChatType.CHAT);
                liveCodingChatDomain.setUsernum(userNum);
                liveCodingChatDomain.setMsg(payload);
                String msgObj = objectMapper.writeValueAsString(liveCodingChatDomain);
                s.sendMessage(new TextMessage(msgObj));
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

        // ✅ 1. WebSocket을 통해 FE에 즉시 전송
        broadcastMessage(userNum, roomId, LiveCodingAction.JOIN.getAction());
        System.out.println("✅ 사용자 입장: " + userNum);
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
            // ✅ 방장이 나가면 전체 세션 종료
            for (WebSocketSession s : new HashSet<>(sessions)) {
                try {
                    s.close(CloseStatus.GOING_AWAY);
                } catch (IOException e) {
                    throw new CmcException("LCD014");
                }
            }
            rooms.remove(roomId);
            liveCodingUseCase.deleteLiveCoding(UUID.fromString(roomId));

            System.out.println("🚫 방 종료: " + roomId);
        } else {
            sessions.remove(session);

            // ✅ 1. WebSocket을 통해 FE에 즉시 전송
            broadcastMessage(userNum, roomId, LiveCodingAction.LEAVE.getAction());
            liveCodingUseCase.updateLiveCoding(UUID.fromString(roomId), userNum, LiveCodingAction.LEAVE.getAction());
            System.out.println("❌ 사용자 퇴장: " + userNum);
        }
    }

    // 입퇴장 용
    private void broadcastMessage(Long userNum, String roomId, int action) {
        Set<WebSocketSession> roomSessions = rooms.getOrDefault(roomId, Set.of());
        for (WebSocketSession s : roomSessions) {
            if (s.isOpen()) {
                try {
                    LiveCodingChatDomain liveCodingChatDomain = new LiveCodingChatDomain();
                    liveCodingChatDomain.setAction(action);
                    liveCodingChatDomain.setLiveCodingChatType(LiveCodingChatType.IN_OUT);
                    liveCodingChatDomain.setUsernum(userNum);

                    String msgObj = objectMapper.writeValueAsString(liveCodingChatDomain);
                    s.sendMessage(new TextMessage(msgObj));
                } catch (IOException e) {
                    throw new CmcException("LCD015");
                }
            }
        }
    }




}