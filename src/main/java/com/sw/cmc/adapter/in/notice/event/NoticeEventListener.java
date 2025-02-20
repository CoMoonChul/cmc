package com.sw.cmc.adapter.in.notice.event;

import com.sw.cmc.adapter.out.notice.persistence.NoticeTemplateRepository;
import com.sw.cmc.application.port.in.notice.NoticeUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.entity.Notification;
import com.sw.cmc.entity.NotificationTemplate;
import com.sw.cmc.event.notice.SendNotiInAppEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * packageName    : com.sw.cmc.adapter.in.notice.event
 * fileName       : NoticeEventListener
 * author         : An Seung Gi
 * date           : 2025-02-19
 * description    :
 */

@Component
@RequiredArgsConstructor
public class NoticeEventListener {
    private final NoticeUseCase noticeUseCase;
    private final NoticeTemplateRepository noticeTemplateRepository;
    private final MessageUtil messageUtil;

    @EventListener
    @Transactional
    public void handleNotification(SendNotiInAppEvent event) throws Exception {
        // 1. NotificationTemplate 조회 (DB에서 가져오기)
        NotificationTemplate template = noticeTemplateRepository.findById(event.getNotiTemplateId())
                .orElseThrow(() -> new CmcException(messageUtil.getMessage("NOTITEMPLATE")));


        Notification notification = Notification.builder()
                .userNum(event.getUserNum())
                .sendAt(event.getSendAt())
                .linkUrl(event.getLinkUrl())
                .notiTemplate(template)
                .createUser(event.getCreateUser())
                .createdAt(LocalDateTime.now().toString()) // 현재 시간 설정
                .sendState(event.getSendState())
                .build();

        noticeUseCase.saveNotification(notification);
    }
}
