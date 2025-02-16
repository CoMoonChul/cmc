package com.sw.cmc.application.service.smtp;

import com.sw.cmc.application.port.in.smtp.SmtpUseCase;
import com.sw.cmc.domain.smtp.SendEmailDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.sw.cmc.application.service
 * fileName       : SmtpService
 * author         : ihw
 * date           : 2025. 2. 16.
 * description    : smtp 서비스
 */
@Service
@RequiredArgsConstructor
public class SmtpService implements SmtpUseCase {
    private final MailSender mailSender;

    @Override
    public void handleSendNotiEmail(SendEmailDomain sendEmailDomain) throws Exception {
        SimpleMailMessage msg = new SimpleMailMessage();
        // 받는 사람 이메일
        msg.setTo(sendEmailDomain.getRsvrEmail());
        // 이메일 제목
        msg.setSubject(sendEmailDomain.getSubject());
        // 이메일 내용
        msg.setText(sendEmailDomain.getText());

        mailSender.send(msg);
    }
}
