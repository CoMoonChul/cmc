package com.sw.cmc.application.port.in.notice;


import com.sw.cmc.domain.notice.NotiListDomain;
import com.sw.cmc.domain.notice.NoticeDomain;
import com.sw.cmc.entity.Notification;

import java.util.List;

/**
 * packageName    : com.sw.cmc.application.port.in.notice
 * fileName       : NoticeUseCase
 * author         : An Seung Gi
 * date           : 2025-02-15
 * description    :
 */
public interface NoticeUseCase {

    /**
     * methodName : selectNotice
     * author : AN SEUNG GI
     * description :
     *
     * @return notice domain
     * @throws Exception the exception
     */
    List<Notification> selectNotice(String userNum) throws Exception;

    NotiListDomain selectPageNotice(String userNum, Integer page, Integer size) throws Exception;

    void saveNotification(Notification notification) throws Exception;

    NoticeDomain deleteNotice(NoticeDomain noticeDomain) throws Exception;
}
