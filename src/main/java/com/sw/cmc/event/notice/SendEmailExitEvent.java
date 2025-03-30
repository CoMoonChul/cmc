package com.sw.cmc.event.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.event.notice
 * fileName       : SendEmailExitEvent
 * author         : An Seung Gi
 * date           : 2025-03-27
 * description    : 회원탈퇴 이메일 객체
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailExitEvent {
    private String to;
    private String userName;
}
