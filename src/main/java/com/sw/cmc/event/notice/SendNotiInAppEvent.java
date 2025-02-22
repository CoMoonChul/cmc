package com.sw.cmc.event.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.sw.cmc.event.notice
 * fileName       : SendNotiInAppEvent
 * author         : dkstm
 * date           : 2025-02-19
 * description    :
 */
@Getter
@AllArgsConstructor
@Builder
public class SendNotiInAppEvent {
    private final String userNum; // 알림 받는 유저
    private final Long notiTemplateId; // 알림 템플릿 번호
    private final String sendAt; // 발송 시간
    private final String linkUrl; // 이동 url
    private final Long createUser; // 알림 생성 유저
    private final String sendState; // 발송 상태
}
