package com.sw.cmc.event.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.event.notice
 * fileName       : SendNotiEmailHtmlEvent
 * author         : An Seung Gi
 * date           : 2025-02-27
 * description    : 이메일 알림 발송 요청시 사용 클래스
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendNotiEmailHtmlEvent {
    private String to;

    private String subject;

    private String title;

    private String message;

    private String link;
}
