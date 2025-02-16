package com.sw.cmc.application.port.in.smtp;

import com.sw.cmc.domain.smtp.SendEmailDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.smtp
 * fileName       : SmtpUseCase
 * author         : ihw
 * date           : 2025. 2. 16.
 * description    : smtp usecase
 */
public interface SmtpUseCase {
    void handleSendNotiEmail(SendEmailDomain sendEmailDomain) throws Exception;
}
