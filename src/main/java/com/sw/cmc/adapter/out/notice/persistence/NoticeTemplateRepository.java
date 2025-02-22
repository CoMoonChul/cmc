package com.sw.cmc.adapter.out.notice.persistence;

import com.sw.cmc.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.sw.cmc.adapter.out.notice.persistence
 * fileName       : NoticeTemplateRepository
 * author         : dkstm
 * date           : 2025-02-19
 * description    :
 */
public interface NoticeTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
}
