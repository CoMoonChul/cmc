package com.sw.cmc.common.util;

import com.sw.cmc.event.notice.SendNotiInAppEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * packageName    : com.sw.cmc.common.util
 * fileName       : NotiUtil
 * author         : An Seung Gi
 * date           : 2025-03-27
 * description    : 공통 알림 유틸
 */

@Component
@RequiredArgsConstructor
public class NotiUtil {

    private final UserUtil userUtil;
    private final ApplicationEventPublisher eventPublisher;

    public void sendNotice(Long notiTemplateId, String linkUrl , Map<String, String> templateParams) {
        // 인앱 알림 (notice 테이블에 저장)
        Long userNum = userUtil.getAuthenticatedUserNum();

        SendNotiInAppEvent sendNotiInAppEvent = SendNotiInAppEvent.builder()
                .notiTemplateId(notiTemplateId)
                .sendAt(LocalDateTime.now().toString())
                .linkUrl(linkUrl)
                .createUser(userNum)
                .sendState("Y") // 전송상태 관리 하려 했으나 필요없을듯?
                .templateParams(templateParams)
                .build();

        eventPublisher.publishEvent(sendNotiInAppEvent);
    }
}
