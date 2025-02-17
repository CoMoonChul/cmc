package com.sw.cmc.adapter.out.lcd.persistence;

import com.sw.cmc.adapter.in.livecode.dto.LiveCoding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * packageName    : com.sw.cmc.adapter.out.persistence.repository
 * fileName       : LiveCodingRepository
 * author         : Ko
 * date           : 2025-02-08
 * description    : LiveCoding Repository
 */
@Repository
public interface LiveCodingRepository extends JpaRepository<LiveCoding, Long> {
    LiveCoding findByHostId(Long hostId);
}