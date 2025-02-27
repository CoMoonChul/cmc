package com.sw.cmc.application.service.smtp;

import com.sw.cmc.application.port.in.smtp.SmtpUseCase;
import com.sw.cmc.domain.smtp.SendEmailDomain;
import com.sw.cmc.domain.smtp.SendEmailHtmlDomain;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

import org.apache.commons.validator.routines.EmailValidator;



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
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

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

    @Override
    public void sendHtmlEmail(SendEmailHtmlDomain sendEmailHtmlDomain) throws Exception {
        // 📌 Thymeleaf에서 사용할 변수 설정
        Context context = new Context();
        context.setVariable("title", sendEmailHtmlDomain.getTitle());
        context.setVariable("message", sendEmailHtmlDomain.getMessage());
        context.setVariable("link", sendEmailHtmlDomain.getLink());

        // 📌 템플릿에서 HTML 생성
        String htmlContent = templateEngine.process("email-template", context);

        // 📌 이메일 전송
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String recipientEmail = sendEmailHtmlDomain.getTo();
        if (recipientEmail == null || recipientEmail.trim().isEmpty() || !EmailValidator.getInstance().isValid(recipientEmail.trim())) {
            throw new IllegalArgumentException("잘못된 이메일 주소: " + recipientEmail);
        }

        helper.setTo(recipientEmail.trim()); // 앞뒤 공백 제거 후 설정

        helper.setTo(sendEmailHtmlDomain.getTo());
        helper.setSubject(sendEmailHtmlDomain.getSubject());
        helper.setText(htmlContent, true); // HTML 형식 적용

        javaMailSender.send(mimeMessage);
    }




}
