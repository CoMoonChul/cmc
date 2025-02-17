package com.sw.cmc.adapter.out.lcd.persistence;

import com.sw.cmc.domain.lcd.LiveChatDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * packageName    : com.sw.cmc.adapter.out.persistence.repository
 * fileName       : LiveChatRepository
 * author         : Ko
 * date           : 2025-02-08
 * description    : LiveChat repository
 */


@Repository
public interface LiveChatRepository extends JpaRepository<LiveChatDomain, Long> {
}
