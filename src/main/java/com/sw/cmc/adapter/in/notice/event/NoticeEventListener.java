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
                .orElseThrow(() -> new CmcException("NOTI001"));

        String notiTemplate = template.getNotiContent();
        String reasonNoti = replacePlaceholders(notiTemplate, event.getTemplateParams());

        NoticeDomain noticeDomain = NoticeDomain.builder()
                .userNum(event.getUserNum())
                .sendAt(event.getSendAt())
                .linkUrl(event.getLinkUrl())
                .notiTemplate(template)
                .createUser(event.getCreateUser())
                .createdAt(LocalDateTime.now().toString()) // 현재 시간 설정
                .sendState(event.getSendState())
                .reasonNoti(reasonNoti)
                .build();

        noticeUseCase.saveNotification(noticeDomain);
    }

    private String replacePlaceholders(String template, Map<String, Long> values) {
        // 정규 표현식을 사용하여 {}로 감싸진 부분을 찾음
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(template);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String placeholder = matcher.group(1); // {} 내부의 텍스트를 추출
            Long replacement = values.get(placeholder); // 대체할 값 찾기
            if (replacement == null) {
                ///
            }
            matcher.appendReplacement(result, StringUtils.defaultString(String.valueOf(replacement), placeholder));
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
