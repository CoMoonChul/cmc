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
    private final ConcurrentHashMap<WebSocketSession, String> sessionToRoomMap = new ConcurrentHashMap<>();

    /** 방에 세션 추가 */
    public void addSession(String roomId, WebSocketSession session) {
        rooms.computeIfAbsent(roomId, k -> Collections.synchronizedSet(new HashSet<>())).add(session);
        sessionToRoomMap.put(session, roomId);
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
        sessionToRoomMap.remove(session);
    }

    /** 세션만으로 제거 (roomId 모를 때 사용) */
    public void removeSession(WebSocketSession session) {
        String roomId = sessionToRoomMap.get(session);
        if (roomId != null) {
            removeSession(roomId, session);
        }
    }

    /** 세션으로부터 roomId 조회 */
    public String getRoomIdBySession(WebSocketSession session) {
        return sessionToRoomMap.get(session);
    }

    public Set<WebSocketSession> getSessions(String roomId) {
        return rooms.getOrDefault(roomId, Collections.emptySet());
    }

    public Set<WebSocketSession> getRoomSessions(String roomId) {
        return getSessions(roomId);
    }

    /** 특정 방 제거 */
    public void removeRoom(String roomId) {
        Set<WebSocketSession> sessions = rooms.remove(roomId);
        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                sessionToRoomMap.remove(session);
            }
        }
    }
}