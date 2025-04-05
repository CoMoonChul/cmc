package com.sw.cmc.adapter.in.lcd.web;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * packageName    : com.sw.cmc.adapter.in.lcd.web
 * fileName       : WebSocketRoomManager
 * author         : Ko
 * date           : 2025-04-05
 * description    : WebSocketRoomManager
 */
@Component
public class WebSocketRoomManager {
    private final ConcurrentHashMap<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    /** 방에 세션 추가 */
    public void addSession(String roomId, WebSocketSession session) {
        rooms.computeIfAbsent(roomId, k -> Collections.synchronizedSet(new HashSet<>())).add(session);
    }

    /** 방에서 세션 제거 */
    public void removeSession(String roomId, WebSocketSession session) {
        Set<WebSocketSession> sessions = rooms.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                rooms.remove(roomId);
            }
        }
    }

    /** 방에 있는 세션들 조회 (기존) */
    public Set<WebSocketSession> getSessions(String roomId) {
        return rooms.getOrDefault(roomId, Collections.emptySet());
    }

    /** ✅ broadcast 쪽에서 사용하는 명확한 이름 alias */
    public Set<WebSocketSession> getRoomSessions(String roomId) {
        return getSessions(roomId);
    }

    /** 전체 방 ID 조회 */
    public Set<String> getAllRoomIds() {
        return rooms.keySet();
    }

    /** 특정 방 제거 */
    public void removeRoom(String roomId) {
        rooms.remove(roomId);
    }
}