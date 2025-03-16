package com.sw.cmc.adapter.in.lcd.web;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
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

    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        String roomId = getRoomId(session);
        rooms.putIfAbsent(roomId, ConcurrentHashMap.newKeySet());
        rooms.get(roomId).add(session);
        System.out.println("✅ WebSocket 연결됨: " + roomId);
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        String roomId = getRoomId(session);
        String payload = message.getPayload();

        // 받은 메시지를 같은 방의 모든 유저에게 전송
        for (WebSocketSession s : rooms.getOrDefault(roomId, Set.of())) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(payload));
            }
        }
    }


    private String getRoomId(WebSocketSession session) {
        try {
            String[] pathSegments = Objects.requireNonNull(session.getUri()).getPath().split("/");
            if (pathSegments.length >= 4) {
                return pathSegments[3];
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            System.err.println("⚠️ WebSocket 연결 오류: 올바른 roomId를 찾을 수 없음.");
        }
        return "";
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        String roomId = getRoomId(session);
        Set<WebSocketSession> sessions = rooms.getOrDefault(roomId, Set.of());

        sessions.remove(session);
        if (sessions.isEmpty()) {
            rooms.remove(roomId); // ✅ 빈 방 제거
        }

        System.out.println("❌ WebSocket 연결 종료: " + roomId);
    }
}