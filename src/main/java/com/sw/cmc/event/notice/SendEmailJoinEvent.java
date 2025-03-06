package com.sw.cmc.event.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.event.notice
 * fileName       : SendEmailJoinEvent
 * author         : An Seung Gi
 * date           : 2025-03-06
 * description    : 코문철 회원가입 이벤트 객체
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailJoinEvent {
    private String to;
    private String userName;
    private String userId;
    private String passWord;
}
