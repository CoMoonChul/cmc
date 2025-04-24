package com.sw.cmc.adapter.in.lcd.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.domain.lcd.LiveCodingAction;
import com.sw.cmc.domain.lcd.LiveCodingChatDomain;
import com.sw.cmc.domain.lcd.LiveCodingChatType;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

/**
 * packageName    : com.sw.cmc.adapter.in.lcd
 * fileName       : WebSocketController
 * author         : 82104
 * date           : 2025-02-11
 * description    : 웹소켓 컨트롤러
 */
@Component
public class WebSocketControllerImpl extends TextWebSocketHandler {
    private final LiveCodingUseCase liveCodingUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocketBroadcaster webSocketBroadcaster;
    private final WebSocketRoomManager webSocketRoomManager;

    public WebSocketControllerImpl(
            LiveCodingUseCase liveCodingUseCase,
            WebSocketBroadcaster webSocketBroadcaster,
            WebSocketRoomManager webSocketRoomManager
    ) {
        this.liveCodingUseCase = liveCodingUseCase;
        this.webSocketBroadcaster = webSocketBroadcaster;
        this.webSocketRoomManager = webSocketRoomManager;
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        Long userNum = (Long) session.getAttributes().get("userNum");
        String userName = (String) session.getAttributes().get("userName");
        String roomId = webSocketRoomManager.getRoomIdBySession(session); // ✅ 변경됨

        if (roomId == null || roomId.isEmpty()) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        String payload = message.getPayload();
        Set<WebSocketSession> roomSessions = webSocketRoomManager.getSessions(roomId);

        for (WebSocketSession s : roomSessions) {
            if (s.isOpen()) {
                LiveCodingChatDomain liveCodingChatDomain = new LiveCodingChatDomain();
                liveCodingChatDomain.setLiveCodingChatType(LiveCodingChatType.CHAT.getType());
                liveCodingChatDomain.setUsernum(userNum);
                liveCodingChatDomain.setUsername(userName);
                liveCodingChatDomain.setMsg(payload);
                String msgObj = objectMapper.writeValueAsString(liveCodingChatDomain);
                s.sendMessage(new TextMessage(msgObj));
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
        String userName = (String) session.getAttributes().get("userName");

        if (userNum == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
            throw new CmcException("LCD013");
        }

        String roomId = getRoomId(session);
        if (roomId == null || roomId.isEmpty()) {
            session.close(CloseStatus.BAD_DATA);
            throw new CmcException("LCD001");
        }

        try {
            webSocketRoomManager.addSession(roomId, session);
            LiveCodingDomain roomInfo = liveCodingUseCase.selectLiveCoding(UUID.fromString(roomId));
            boolean isInvited = roomInfo.getParticipants().contains(userNum);
            if (!isInvited) {
                webSocketRoomManager.removeSession(session);
                session.close(CloseStatus.NOT_ACCEPTABLE);
                return;
            }

            webSocketBroadcaster.broadcastMessage(userNum, userName, roomId, LiveCodingAction.JOIN.getAction(), null);
            System.out.println("✅ 사용자 입장: " + userName);

        } catch (Exception e) {
            webSocketRoomManager.removeSession(session);
            throw new CmcException("LCD019");
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        Long userNum = (Long) session.getAttributes().get("userNum");
        String userName = (String) session.getAttributes().get("userName");
        if (userNum == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
            throw new CmcException("LCD013");
        }

        String roomId = webSocketRoomManager.getRoomIdBySession(session); // ✅ session -> roomId 사용
        if (roomId == null || roomId.isEmpty()) {
            return;
        }

        webSocketRoomManager.removeSession(session); // ✅ 간편화된 제거

        // 3초 뒤 퇴장 판단
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Set<WebSocketSession> remainingSessions = webSocketRoomManager.getSessions(roomId);

                boolean isStillConnected = remainingSessions.stream()
                        .anyMatch(s -> {
                            Long uid = (Long) s.getAttributes().get("userNum");
                            return uid != null && uid.equals(userNum);
                        });

                if (isStillConnected) {
                    System.out.println("🔁 유저 재접속 감지됨: " + userNum);
                    return;
                }

                try {
                    LiveCodingDomain roomInfo = liveCodingUseCase.selectLiveCoding(UUID.fromString(roomId));
                    boolean isHost = userNum.equals(roomInfo.getHostId());

                    if (isHost) {
                        Set<WebSocketSession> targetSessions = new HashSet<>(remainingSessions);
                        webSocketRoomManager.removeRoom(roomId);
                        liveCodingUseCase.deleteLiveCoding(UUID.fromString(roomId));
                        webSocketBroadcaster.broadcastMessage(userNum, userName, roomId, LiveCodingAction.DELETE.getAction(), targetSessions);
                        System.out.println("🚫 호스트 완전 퇴장 → 방 삭제됨: " + roomId);
                    } else {
                        webSocketBroadcaster.broadcastMessage(userNum, userName, roomId, LiveCodingAction.LEAVE.getAction(), null);
                        liveCodingUseCase.updateLiveCoding(UUID.fromString(roomId), userNum, LiveCodingAction.LEAVE.getAction());
                        System.out.println("❌ 게스트 완전 퇴장: " + userNum);
                    }

                } catch (Exception e) {
                    throw new CmcException("LCD022");
                }
            }
        }, 2000);
    }
}