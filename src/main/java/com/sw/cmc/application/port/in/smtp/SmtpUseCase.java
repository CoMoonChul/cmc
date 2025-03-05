package com.sw.cmc.application.port.in.smtp;
import com.sw.cmc.domain.smtp.SendEmailHtmlDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.smtp
 * fileName       : SmtpUseCase
 * author         : ihw
 * date           : 2025. 2. 16.
 * description    : smtp usecase
 */
public interface SmtpUseCase {
    void sendHtmlEmail(SendEmailHtmlDomain sendEmailHtmlDomain) throws Exception;
}
