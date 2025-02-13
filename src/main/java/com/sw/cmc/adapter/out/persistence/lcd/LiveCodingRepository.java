package com.sw.cmc.adapter.out.persistence.lcd;

import com.sw.cmc.domain.lcd.LiveCoding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.sw.cmc.adapter.out.persistence.repository
 * fileName       : LiveCodingRepository
 * author         : Ko
 * date           : 2025-02-08
 * description    :
 */
public interface LiveCodingRepository extends JpaRepository<LiveCoding, Long> {
    // 특정 ID로 LiveCoding 조회
    Optional<LiveCoding> findById(Long id);
}