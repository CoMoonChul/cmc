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
     * methodName : selectPageNotice
     * author : AN SEUNG GI
     * description : 알림 리스트 조회 (페이징)
     *
     * @param user num
     * @param page
     * @param size
     * @return noti list domain
     * @throws Exception the exception
     */
    NotiListDomain selectPageNotice(Integer page, Integer size) throws Exception;


    /**
     * methodName : saveNotification
     * author : AN SEUNG GI
     * description : 알림 등록
     *
     * @param notification
     * @throws Exception the exception
     */
    NoticeDomain saveNotification(NoticeDomain noticeDomain) throws Exception;


    /**
     * methodName : deleteNotice
     * author : AN SEUNG GI
     * description : 알림 삭제
     *
     * @param notice domain
     * @return notice domain
     * @throws Exception the exception
     */
    NoticeDomain deleteNotice(NoticeDomain noticeDomain) throws Exception;


    /**
     * methodName : deleteAllNotice
     * author : AN SEUNG GI
     * description : 알림 전체 삭제
     *
     * @return int
     * @throws Exception the exception
     */
    int deleteAllNotice() throws Exception;
}
