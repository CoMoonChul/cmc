package com.sw.cmc.application.service.smtp;
import com.sw.cmc.application.port.in.smtp.SmtpUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.domain.smtp.SendEmailHtmlDomain;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    @Override
    public void sendHtmlEmail(SendEmailHtmlDomain sendEmailHtmlDomain) throws Exception {
        Context context = new Context();
        context.setVariable("title", sendEmailHtmlDomain.getTitle());
        context.setVariable("message", sendEmailHtmlDomain.getMessage());
        context.setVariable("link", sendEmailHtmlDomain.getLink());
        // email-template.html 파일에 내용 담기
        String htmlContent = templateEngine.process("email-template", context);
        // 이메일 전송
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        String recipientEmail = sendEmailHtmlDomain.getTo();
        // 이메일 형식 인지 체크
        if (recipientEmail == null || !EmailValidator.getInstance().isValid(recipientEmail.trim())) {
            throw new CmcException("SMTP001");
        }
        helper.setTo(recipientEmail.trim()); // 앞뒤 공백 제거 후 설정
        helper.setSubject(sendEmailHtmlDomain.getSubject());
        helper.setText(htmlContent, true); // HTML 형식 적용
        javaMailSender.send(mimeMessage);
    }
}
