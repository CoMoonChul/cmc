package com.sw.cmc.domain.smtp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.domain.smtp
 * fileName       : SendEmailDomain
 * author         : ihw
 * date           : 2025. 2. 16.
 * description    : 이메일 발송 도메인
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailDomain {
    // 수신자 이메일
    private String rsvrEmail;
    // 이메일 제목
    private String subject;
    // 이메일 내용
    private String text;
}