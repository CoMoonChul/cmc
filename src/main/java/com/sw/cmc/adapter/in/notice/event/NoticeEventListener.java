package com.sw.cmc.adapter.in.notice.event;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import com.sw.cmc.adapter.out.notice.persistence.NoticeTemplateRepository;
import com.sw.cmc.application.port.in.notice.NoticeUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.domain.notice.NoticeDomain;
import com.sw.cmc.entity.Notification;
import com.sw.cmc.entity.NotificationTemplate;
import com.sw.cmc.event.notice.SendNotiInAppEvent;
import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


}
