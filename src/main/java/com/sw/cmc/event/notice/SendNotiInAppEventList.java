package com.sw.cmc.event.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * packageName    : com.sw.cmc.event.notice
 * fileName       : SendNotiInAppEventList
 * author         : ihw
 * date           : 2025. 4. 9.
 * description    : 다수의 유저에게 동일한 알림이 나가는 경우
 */
@Getter
@AllArgsConstructor
@Builder
public class SendNotiInAppEventList {
    private final List<Long> userNumList; // 알림 받는 유저 배열
    private final Long notiTemplateId; // 알림 템플릿 번호
    private final String sendAt; // 발송 시간
    private final String linkUrl; // 이동 url
    private final Long createUser; // 알림 생성 유저
    private final String sendState; // 발송 상태
    private final Map<String, String> templateParams;
}
