package com.sw.cmc.event.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.event.notice
 * fileName       : SendEmailGroupInviteEvent
 * author         : An Seung Gi
 * date           : 2025-03-06
 * description    : 코문철 그룹 초대 이벤트 객체
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailGroupInviteEvent {
    private String to;
    private String targetName;
    private String userName;
    private String groupName;
}
