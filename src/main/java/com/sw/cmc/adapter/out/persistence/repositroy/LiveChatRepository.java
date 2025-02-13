package com.sw.cmc.adapter.out.persistence.repositroy;

import com.sw.cmc.domain.lcd.LiveChat;
import com.sw.cmc.domain.lcd.LiveCodingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * packageName    : com.sw.cmc.adapter.out.persistence.repository
 * fileName       : LiveChatRepository
 * author         : Ko
 * date           : 2025-02-08
 * description    :
 */


public interface LiveChatRepository extends JpaRepository<LiveChat, Long> {
    // 특정 LiveCodingRoom에 속한 모든 채팅 메시지를 조회
    List<LiveChat> findByLiveCodingRoom(LiveCodingRoom liveCodingRoom);
}
