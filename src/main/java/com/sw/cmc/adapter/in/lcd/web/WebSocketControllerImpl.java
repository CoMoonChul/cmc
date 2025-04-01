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

    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private final Map<Long, Long> reconnectTimers = new ConcurrentHashMap<>(); // 재접속 확인용
    private final LiveCodingUseCase liveCodingUseCase;

    public WebSocketControllerImpl(LiveCodingUseCase liveCodingUseCase) {
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

        for (WebSocketSession s : roomSessions) {
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
        } catch (Exception e) {
            throw new CmcException("LCD001");

        }
        return "";
    }

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

        // 재접속한 경우 타이머 취소
        if (reconnectTimers.containsKey(userNum)) {
            reconnectTimers.remove(userNum);
            System.out.println("✅ 사용자(" + userNum + ")가 재접속하여 삭제 대기 해제됨: " + roomId);
        }

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
        Set<WebSocketSession> sessions = rooms.getOrDefault(roomId, Collections.newSetFromMap(new ConcurrentHashMap<>()));

        sessions.remove(session);

        boolean isHost = userNum.equals(roomInfo.getHostId());

        System.out.println("⏳ 사용자(" + userNum + ") 연결 해제됨: " + roomId + " (1분 대기)");

        // 사용자 제거를 1분 동안 보류하는 로직 실행
        startReconnectionTimer(roomId, userNum, isHost);
    }

    private void startReconnectionTimer(String roomId, Long userNum, boolean isHost) {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 새로고침 방지

                if (isUserReconnected(roomId, userNum)) {
                    System.out.println("🔄 사용자(" + userNum + ")가 빠르게 재접속하여 방 유지됨: " + roomId);
                    return;
                }

                reconnectTimers.put(userNum, System.currentTimeMillis());

                if (isUserReconnected(roomId, userNum)) {
                    System.out.println("✅ 사용자(" + userNum + ")가 1분 내 재접속하여 방 유지됨: " + roomId);
                    reconnectTimers.remove(userNum);
                } else {
                    System.out.println("🚫 사용자(" + userNum + ")가 1분 내 미복귀 → 완전 제거: " + roomId);
                    removeUserFromRoom(roomId, userNum, isHost);
                }
            } catch (InterruptedException e) {
                throw new CmcException("LCD015");
            } catch (Exception e) {
                throw new CmcException("LCD016");
            }
        }).start();
    }

    private boolean isUserReconnected(String roomId, Long userNum) {
        Set<WebSocketSession> updatedSessions = rooms.getOrDefault(roomId, Collections.emptySet());
        return updatedSessions.stream().anyMatch(s -> {
            Long sessionUserNum = (Long) s.getAttributes().get("userNum");
            return sessionUserNum != null && sessionUserNum.equals(userNum);
        });
    }

    private void removeUserFromRoom(String roomId, Long userNum, boolean isHost) throws Exception {
        Set<WebSocketSession> sessions = rooms.getOrDefault(roomId, Collections.emptySet());
        sessions.removeIf(s -> {
            Long sessionUserNum = (Long) s.getAttributes().get("userNum");
            return sessionUserNum != null && sessionUserNum.equals(userNum);
        });

        reconnectTimers.remove(userNum);

        if (isHost) {
            System.out.println("🚨 방장(" + userNum + ") 미복귀 → 방 삭제: " + roomId);
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
        } else {
            System.out.println("👤 참가자(" + userNum + ") 미복귀 → 방에서 제거됨: " + roomId);
            liveCodingUseCase.updateLiveCoding(UUID.fromString(roomId), userNum, LiveCodingAction.LEAVE.getAction());
        }
    }
}
