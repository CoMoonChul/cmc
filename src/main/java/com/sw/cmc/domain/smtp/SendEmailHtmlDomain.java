package com.sw.cmc.domain.smtp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.domain.smtp
 * fileName       : SendEmailHtmlDomain
 * author         : An Seung Gi
 * date           : 2025-02-26
 * description    : 이메일 템플릿 전송 도메인 객체
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailHtmlDomain {
    private String to;

    private String subject;

    private String title;

    private String message;

    private String link;
}
