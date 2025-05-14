package com.sw.cmc.application.service.async;

import com.sw.cmc.adapter.out.group.persistence.GroupMemberRepository;
import com.sw.cmc.common.util.NotiUtil;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.review.ReviewDomain;
import com.sw.cmc.entity.Review;
import com.sw.cmc.event.ai.CreateCommentEvent;
import com.sw.cmc.event.ai.DeleteCommentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : com.sw.cmc.application.service.async
 * fileName       : CmcAsyncService
 * author         : ihw
 * date           : 2025. 6. 1.
 * description    : cmc async service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CmcAsyncService {
    private final GroupMemberRepository groupMemberRepository;
    private final NotiUtil notiUtil;
    private final ApplicationEventPublisher eventPublisher;
    private final UserUtil userUtil;

    @Async("asyncExecutor")
    public void sendNotificationAndPublishEvent(Review saved, List<Long> groupIds, Long authenticatedUserNum, String getAuthenticatedUsername) {
        try {
            if (CollectionUtils.isNotEmpty(groupIds)) {
                List<Long> userIds = groupMemberRepository.findDistinctUserNumsByGroupIds(groupIds);
                userIds.remove(authenticatedUserNum);

                Map<String, String> templateParams = new HashMap<>();
                templateParams.put("userNm", getAuthenticatedUsername);
                templateParams.put("title", saved.getTitle());
                notiUtil.sendNoticeList(authenticatedUserNum, userIds, 7L, "/review/detail/" + saved.getReviewId(), templateParams);
            }

            CreateCommentEvent createCommentEvent = CreateCommentEvent.builder()
                    .reviewId(saved.getReviewId())
                    .title(saved.getTitle())
                    .content(saved.getContent())
                    .codeContent(saved.getCodeContent())
                    .codeType(saved.getCodeType())
                    .build();
            eventPublisher.publishEvent(createCommentEvent);
        } catch (Exception e) {
            log.error("알림 발송 및 이벤트 퍼블리싱 실패", e);
        }
    }

    @Async("asyncExecutor")
    public void sendDeleteReviewEvent(ReviewDomain reviewDomain) {
        DeleteCommentEvent event = DeleteCommentEvent.builder()
                .reviewId(reviewDomain.getReviewId())
                .build();
        eventPublisher.publishEvent(event);
    }
}
