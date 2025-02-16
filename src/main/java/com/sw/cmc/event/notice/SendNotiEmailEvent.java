package com.sw.cmc.event.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.event.notice
 * fileName       : SendNotiEmail
 * author         : ihw
 * date           : 2025. 2. 16.
 * description    : 이메일로 알림 발송 요청 시 사용 클래스
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendNotiEmailEvent {
    private String rsvrEmail;
    private String subject;
    private String text;
}
