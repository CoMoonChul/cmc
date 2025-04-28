package com.sw.cmc.adapter.in.notice.event;

import com.sw.cmc.application.port.in.notice.NoticeUseCase;
import com.sw.cmc.domain.notice.NoticeDomain;
import com.sw.cmc.event.notice.SendNotiInAppEvent;
import com.sw.cmc.event.notice.SendNotiInAppEventList;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * packageName    : com.sw.cmc.adapter.in.notice.event
 * fileName       : NoticeEventListener
 * author         : An Seung Gi
 * date           : 2025-02-19
 * description    : 알림 이벤트 리스너
 */

@Component
@RequiredArgsConstructor
public class NoticeEventListener {
    private final NoticeUseCase noticeUseCase;
    @EventListener
    public void handleNotification(SendNotiInAppEvent event) throws Exception {
        NoticeDomain noticeDomain = NoticeDomain.builder()
                .userNum(event.getUserNum())
                .sendAt(event.getSendAt())
                .linkUrl(event.getLinkUrl())
                .notiTemplateId(event.getNotiTemplateId())
                .createUser(event.getCreateUser())
                .createdAt(LocalDateTime.now().toString()) // 현재 시간 설정
                .sendState(event.getSendState())
                .templateParams(event.getTemplateParams())
                .build();

        noticeUseCase.saveNotification(noticeDomain);
    }

    @EventListener
    public void handleNotificationList(SendNotiInAppEventList event) throws Exception {
        NoticeDomain noticeDomain = NoticeDomain.builder()
                .userNumList(event.getUserNumList())
                .sendAt(event.getSendAt())
                .linkUrl(event.getLinkUrl())
                .notiTemplateId(event.getNotiTemplateId())
                .createUser(event.getCreateUser())
                .createdAt(LocalDateTime.now().toString()) // 현재 시간 설정
                .sendState(event.getSendState())
                .templateParams(event.getTemplateParams())
                .build();

        noticeUseCase.saveNotificationList(noticeDomain);
    }
}
