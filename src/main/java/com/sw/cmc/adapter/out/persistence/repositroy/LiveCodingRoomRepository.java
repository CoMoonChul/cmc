package com.sw.cmc.adapter.out.persistence.repositroy;

import com.sw.cmc.domain.lcd.LiveCodingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.sw.cmc.adapter.out.persistence.repository
 * fileName       : LiveCodingRepository
 * author         : Ko
 * date           : 2025-02-08
 * description    :
 */
public interface LiveCodingRoomRepository extends JpaRepository<LiveCodingRoom, Long> {
    // 특정 ID로 LiveCodingRoom 조회
    Optional<LiveCodingRoom> findById(Long id);
}