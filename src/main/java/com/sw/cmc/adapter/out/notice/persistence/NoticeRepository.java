package com.sw.cmc.adapter.out.notice.persistence;

import com.sw.cmc.domain.notice.NotiListDomain;
import com.sw.cmc.domain.notice.NoticeDomain;
import com.sw.cmc.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.sw.cmc.adapter.out.notice.persistence
 * fileName       : NoticeRepository
 * author         : An Seung Gi
 * date           : 2025-02-15
 * description    :
 */
@Repository
public interface NoticeRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT noti FROM Notification noti " +
            "LEFT JOIN FETCH noti.notiTemplate " +
            "WHERE noti.userNum = :userNum")
    Page<Notification> findByUserNum(@Param("userNum") Long userNum, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.userNum = :userNum")
    int deleteByUserNum(@Param("userNum") Long userNum);

}
