package com.sw.cmc.application.service.review;

import com.sw.cmc.adapter.out.comment.persistence.CommentRepository;
import com.sw.cmc.adapter.out.group.persistence.GroupMemberRepository;
import com.sw.cmc.adapter.out.like.persistence.ReviewLikeRepository;
import com.sw.cmc.adapter.out.review.persistence.ReviewRepository;
import com.sw.cmc.adapter.out.view.persistence.ReviewViewRepository;
import com.sw.cmc.application.port.in.review.ReviewUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.NotiUtil;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.review.*;
import com.sw.cmc.entity.Review;
import com.sw.cmc.entity.ReviewView;
import com.sw.cmc.entity.User;
import com.sw.cmc.event.ai.CreateCommentEvent;
import com.sw.cmc.event.ai.DeleteCommentEvent;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * packageName    : com.sw.cmc.application.service.review
 * fileName       : ReviewService
 * author         : Park Jong Il
 * date           : 25. 2. 16.
 * description    : review service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService implements ReviewUseCase {

    private final EntityManager entityManager;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final ReviewViewRepository reviewViewRepository;
    private final UserUtil userUtil;
    private final ReviewLikeRepository reviewLikeRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final NotiUtil notiUtil;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ReviewDomain selectReview(Long reviewId) throws Exception {
        ReviewDetailVo found = reviewRepository.findReviewDetail(reviewId)
                .orElseThrow(() -> new CmcException("REVIEW001"));
        return ReviewDomain.builder()
                .reviewId(found.getReview().getReviewId())
                .username(found.getUsername())
                .userNum(found.getUserNum())
                .userImg(found.getUserImg())
                .title(found.getReview().getTitle())
                .content(found.getReview().getContent())
                .codeContent(found.getReview().getCodeContent())
                .codeType(found.getReview().getCodeType())
                .viewCount(found.getViewCount())
                .likeCount(found.getLikeCount())
                .createdAt(found.getReview().getCreatedAt())
                .updatedAt(found.getReview().getUpdatedAt())
                .build();
    }

    @Override
    public ReviewListDomain selectReviewList(Integer condition, Integer page, Integer size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Long userNum = userUtil.getAuthenticatedUserNum();
        Page<ReviewListVo> res = switch (ReviewListCondition.of(condition)) {
            case LATEST -> reviewRepository.findAllReviews(pageable);
            case MOST_LIKED -> reviewRepository.findAllOrderByLikeCountDesc(pageable);
            case MY_REVIEW -> {
                validateUserNum(userNum);
                yield reviewRepository.findMyReviews(userUtil.getAuthenticatedUserNum(), pageable);
            }
            case MY_COMMENTED -> {
                validateUserNum(userNum);
                yield reviewRepository.findMyCommentingReviews(userUtil.getAuthenticatedUserNum(), pageable);
            }
            case MY_LIKED -> {
                validateUserNum(userNum);
                yield reviewRepository.findMyLikeReviews(userUtil.getAuthenticatedUserNum(), pageable);
            }
        };

        List<ReviewDomain> list = res.getContent().stream()
                .map(s -> ReviewDomain.builder()
                        .reviewId(s.getReview().getReviewId())
                        .title(s.getReview().getTitle())
                        .content(s.getReview().getContent())
                        .createdAt(s.getReview().getCreatedAt())
                        .username(s.getUsername())
                        .userImg(s.getUserImg())
                        .likeCount(s.getLikeCount())
                        .viewCount(s.getViewCount())
                        .build()).toList();
        return ReviewListDomain.builder()
                .pageNumber(res.getPageable().getPageNumber())
                .pageSize(res.getPageable().getPageSize())
                .totalElements(res.getTotalElements())
                .totalPages(res.getTotalPages())
                .reviewList(list)
                .build();
    }

    @Override
    @Transactional
    public ReviewDomain createReview(ReviewDomain reviewDomain) throws Exception {
        reviewDomain.validateCreateAndUpdateReview();

        // 로그인 사용자 userNum
        Long authenticatedUserNum = userUtil.getAuthenticatedUserNum();
        User savingUser = new User();
        savingUser.setUserNum(authenticatedUserNum);

        Review saving = new Review();
        saving.setUser(savingUser);
        saving.setTitle(reviewDomain.getTitle());
        saving.setContent(reviewDomain.getContent());
        saving.setCodeContent(reviewDomain.getCodeContent());
        saving.setCodeType(reviewDomain.getCodeType());

        Review saved = reviewRepository.save(saving);
        entityManager.refresh(saved);

        // 알림 발송 + 이벤트 퍼블리싱 비동기 처리
        sendNotificationAndPublishEvent(saved, reviewDomain.getGroups(), authenticatedUserNum);

        return ReviewDomain.builder()
                .reviewId(saved.getReviewId())
                .userNum(saved.getUser().getUserNum())
                .title(saved.getTitle())
                .content(saved.getContent())
                .codeContent(saved.getCodeContent())
                .codeType(saved.getCodeType())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public ReviewDomain deleteReview(ReviewDomain reviewDomain) throws Exception {
        Review found = reviewRepository.findById(reviewDomain.getReviewId())
                .orElseThrow(() ->new CmcException("REVIEW001"));

        if(!Objects.equals(found.getUser().getUserNum(), userUtil.getAuthenticatedUserNum())) {
            throw new CmcException("REVIEW002");
        }

        reviewRepository.deleteById(found.getReviewId());
        sendDeleteReviewEvent(reviewDomain);
        return reviewDomain;
    }

    @Override
    @Transactional
    public ReviewDomain updateReview(ReviewDomain reviewDomain) throws Exception {
        reviewDomain.validateCreateAndUpdateReview();

        Review found = reviewRepository.findById(reviewDomain.getReviewId())
                .orElseThrow(() -> new CmcException("REVIEW001"));

        if(!Objects.equals(found.getUser().getUserNum(), userUtil.getAuthenticatedUserNum())) {
            throw new CmcException("REVIEW002");
        }
        // 댓글이 존재할 경우 수정 불가
        if (commentRepository.existsByTargetIdAndCommentTarget(found.getReviewId(), 0)) {
            throw new CmcException("REVIEW007");
        }

        // 수정 작업 (제목, 게시글, 수정일자 갱신)
        found.setTitle(reviewDomain.getTitle());
        found.setContent(reviewDomain.getContent());
        found.setCodeContent(reviewDomain.getCodeContent());
        found.setCodeType(reviewDomain.getCodeType());

        Review saved = reviewRepository.save(found);

        // 저장
        return ReviewDomain.builder()
                .reviewId(saved.getReviewId())
                .userNum(saved.getUser().getUserNum())
                .title(saved.getTitle())
                .content(saved.getContent())
                .codeContent(saved.getCodeContent())
                .codeType(saved.getCodeType())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }
    // builder 대신 공통으로 활용하는 생성자
    private ReviewDomain convertEntityToDomain(Review review) {
        // 조회수 조회 및 업데이트
        ReviewView reviewView = reviewViewRepository.findById(review.getReviewId())
                .orElseThrow(() -> new CmcException("REVIEW001"));

        // 좋아요 조회
        Long reviewLike = reviewLikeRepository.countById_ReviewId(review.getReviewId());
        reviewLike = Objects.requireNonNullElse(reviewLike, 0L); // null이면 0으로 설정 (조회수 기본값 0 설정 보류)

        return ReviewDomain.builder()
                .reviewId(review.getReviewId())
                .userNum(review.getUser().getUserNum())
                .title(review.getTitle())
                .content(review.getContent())
                .codeContent(review.getCodeContent())
                .codeType(review.getCodeType())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .viewCount(reviewView.getViewCount())
                .likeCount(reviewLike)
                .build();
    }

    @Async
    private void sendNotificationAndPublishEvent(Review saved, List<Long> groupIds, Long authenticatedUserNum) {
        try {
            if (CollectionUtils.isNotEmpty(groupIds)) {
                List<Long> userIds = groupMemberRepository.findDistinctUserNumsByGroupIds(groupIds);
                userIds.remove(authenticatedUserNum);

                Map<String, String> templateParams = new HashMap<>();
                templateParams.put("userNm", userUtil.getAuthenticatedUsername());
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

    @Async
    private void sendDeleteReviewEvent(ReviewDomain reviewDomain) {
        DeleteCommentEvent event = DeleteCommentEvent.builder()
                .reviewId(reviewDomain.getReviewId())
                .build();
        eventPublisher.publishEvent(event);
    }

    private void validateUserNum(Long userNum) {
        if (userUtil.getAuthenticatedUserNum() == null) {
            throw new CmcException("REVIEW010");
        }
    }
}
