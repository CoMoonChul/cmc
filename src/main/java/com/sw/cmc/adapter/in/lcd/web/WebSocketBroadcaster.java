package com.sw.cmc.adapter.in.lcd.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.cmc.adapter.in.livecoding.dto.UpdateLiveCodingSnippetReqDTOCursorPos;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.domain.lcd.LiveCodingChatDomain;
import com.sw.cmc.domain.lcd.LiveCodingChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;

/**
 * packageName    : com.sw.cmc.adapter.in.lcd.web
 * fileName       : WebSocketBroadcaster
 * author         : Ko
 * date           : 2025-04-05
 * description    : WebSocketBroadcaster
 */
@Component
@RequiredArgsConstructor
public class WebSocketBroadcaster {
    private final ObjectMapper objectMapper;
    private final WebSocketRoomManager webSocketRoomManager; // rooms 관리 클래스

    public void broadcastCodeUpdate(String roomId, Long senderUserNum, Object diffObject,
                                    UpdateLiveCodingSnippetReqDTOCursorPos cursorPos) {
        Set<WebSocketSession> roomSessions = webSocketRoomManager.getSessions(roomId);

        if (roomSessions.isEmpty()) {
            throw new CmcException("LCD016");
        }

        for (WebSocketSession s : roomSessions) {
            if (s.isOpen()) {
                Long targetUserNum = (Long) s.getAttributes().get("userNum");
                if (targetUserNum != null && !targetUserNum.equals(senderUserNum)) {
                    try {
                        LiveCodingChatDomain msg = new LiveCodingChatDomain();
                        msg.setLiveCodingChatType(LiveCodingChatType.UPDATE.getType());
                        msg.setUsernum(senderUserNum);
                        msg.setDiff(objectMapper.writeValueAsString(diffObject));
                        msg.setCursorPos(cursorPos); // ✅ 커서 포지션 포함

                        s.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
                    } catch (IOException e) {
                        throw new CmcException("LCD014");
                    }
                }
            }
        }
    }

    public void broadcastMessage(Long userNum, String roomId, int action, Set<WebSocketSession> roomSessions) {
        Set<WebSocketSession> targetRoomSessions =
                (roomSessions != null) ? roomSessions : webSocketRoomManager.getRoomSessions(roomId);

        for (WebSocketSession s : targetRoomSessions) {
            if (s.isOpen()) {
                try {
                    LiveCodingChatDomain liveCodingChatDomain = new LiveCodingChatDomain();
                    liveCodingChatDomain.setAction(action);
                    liveCodingChatDomain.setLiveCodingChatType(LiveCodingChatType.IN_OUT.getType());
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
