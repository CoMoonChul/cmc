package com.sw.cmc.event.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.event.notice
 * fileName       : SendEmailAccountFindEvent
 * author         : An Seung Gi
 * date           : 2025-03-27
 * description    : 코물철 계정 찾기 이벤트 이메일 객체
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailAccountFindEvent {
    private String to;
    private String userName;
    private String userId;
    private String passWord;
}
