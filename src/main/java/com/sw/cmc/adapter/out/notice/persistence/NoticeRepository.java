package com.sw.cmc.adapter.out.notice.persistence;

import com.sw.cmc.domain.notice.NotiListDomain;
import com.sw.cmc.domain.notice.NoticeDomain;
import com.sw.cmc.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    List<Notification> findAllByUserNum(String userNum);


    @Query("SELECT noti FROM Notification noti " +
            "LEFT JOIN FETCH noti.notiTemplate " +
            "WHERE noti.userNum = :userNum")
    Page<Notification> findByUserNum(@Param("userNum") String userNum, Pageable pageable);


}
