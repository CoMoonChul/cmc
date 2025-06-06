package com.sw.cmc.domain.notice;

import com.sw.cmc.entity.NotificationTemplate;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * packageName    : com.sw.cmc.domain.notice
 * fileName       : NoticeDomain
 * author         : An Seung Gi
 * date           : 2025-02-15
 * description    : 알람 도메인 객체
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDomain {
    private Long notiId;
    private Long userNum;
    private List<Long> userNumList;
    private NotificationTemplate notiTemplate;
    private String sendAt;
    private String sendState;
    private String linkUrl;
    private String createdAt;
    private Long createUser;
    private String notiTemplateNm;
    private String notiTitle;
    private String notiContent;
    private String notiType;
    private String reasonNoti;
    private int deleteCount;
    private Long notiTemplateId;
    private Map<String, String> templateParams;



}
