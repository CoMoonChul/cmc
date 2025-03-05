package com.sw.cmc.adapter.in.smtp.event;

import com.sw.cmc.application.port.in.smtp.SmtpUseCase;
import com.sw.cmc.domain.smtp.SendEmailHtmlDomain;
import com.sw.cmc.event.notice.SendNotiEmailHtmlEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.sw.cmc.adapter.in.smtp.event
 * fileName       : SmtpEventListener
 * author         : ihw
 * date           : 2025. 2. 16.
 * description    : Smtp 이벤트 리스너
 */
@Component
@RequiredArgsConstructor
public class SmtpEventListener {
    private final SmtpUseCase smtpUseCase;
    @EventListener
    public void sendHtmlEmailTest(SendNotiEmailHtmlEvent sendNotiEmailHtmlEvent) throws Exception {
        SendEmailHtmlDomain sendEmailHtmlDomain = SendEmailHtmlDomain.builder()
                .title(sendNotiEmailHtmlEvent.getTitle())
                .message(sendNotiEmailHtmlEvent.getMessage())
                .link(sendNotiEmailHtmlEvent.getLink())
                .to(sendNotiEmailHtmlEvent.getTo())
                .subject(sendNotiEmailHtmlEvent.getSubject())
                .build();
        smtpUseCase.sendHtmlEmail(sendEmailHtmlDomain);
    }
}
