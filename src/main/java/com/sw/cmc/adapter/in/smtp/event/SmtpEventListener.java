package com.sw.cmc.adapter.in.smtp.event;

import com.sw.cmc.application.port.in.smtp.SmtpUseCase;
import com.sw.cmc.domain.smtp.SendEmailDomain;
import com.sw.cmc.event.notice.SendNotiEmailEvent;
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
    public void handleSendNotiEmail(SendNotiEmailEvent sendNotiEmail) throws Exception {
        SendEmailDomain sendEmailDomain = SendEmailDomain.builder()
                .rsvrEmail(sendNotiEmail.getRsvrEmail())
                .subject(sendNotiEmail.getSubject())
                .text(sendNotiEmail.getText())
                .build();
        smtpUseCase.handleSendNotiEmail(sendEmailDomain);
    }
}
