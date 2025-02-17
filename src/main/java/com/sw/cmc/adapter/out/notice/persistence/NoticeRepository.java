package com.sw.cmc.adapter.out.notice.persistence;

import com.sw.cmc.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * packageName    : com.sw.cmc.adapter.out.notice.persistence
 * fileName       : NoticeRepository
 * author         : An Seung Gi
 * date           : 2025-02-15
 * description    :
 */
public interface NoticeRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserNum(String userNum);
}
