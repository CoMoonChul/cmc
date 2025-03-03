package com.sw.cmc.application.service.review;

import com.sw.cmc.adapter.out.comment.persistence.CommentRepository;
import com.sw.cmc.adapter.out.like.persistence.ReviewLikeRepository;
import com.sw.cmc.adapter.out.review.persistence.ReviewRepository;
import com.sw.cmc.adapter.out.view.persistence.ReviewViewRepository;
import com.sw.cmc.application.port.in.review.ReviewUseCase;
import com.sw.cmc.application.service.view.ViewService;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.review.ReviewDomain;
import com.sw.cmc.domain.review.ReviewListDomain;
import com.sw.cmc.domain.view.ReviewViewDomain;
import com.sw.cmc.entity.Review;
import com.sw.cmc.entity.ReviewLike;
import com.sw.cmc.entity.ReviewView;
import com.sw.cmc.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class ReviewService implements ReviewUseCase {

    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final ReviewViewRepository reviewViewRepository;
    private final UserUtil userUtil;
    private final ReviewLikeRepository reviewLikeRepository;

    @Override
    @Transactional
    public ReviewDomain selectReview(Long reviewId) throws Exception {
        Review found = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CmcException("REVIEW001"));
        // 조회수 조회 및 업데이트
        ReviewView reviewView = reviewViewRepository.findById(reviewId)
                .orElseThrow(() -> new CmcException("REVIEW001"));
        // 좋아요 조회
        Long reviewLike = reviewLikeRepository.countById_ReviewId(reviewId);


        return ReviewDomain.builder()
                .reviewId(found.getReviewId())
                .userNum(found.getUser().getUserNum())
                .title(found.getTitle())
                .content(found.getContent())
                .createdAt(found.getCreatedAt())
                .updatedAt(found.getUpdatedAt())
                .codeContent(found.getCodeContent())
                .viewCount(reviewView.getViewCount())
                .likeCount(reviewLike)
                .build();
    }

    @Override
    public ReviewListDomain selectReviewList(Integer page, Integer size, String sort, String order, String keyword) throws Exception {
        // 기본값 설정 제거 (review.yaml에서 설정된 기본값을 사용)
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order), sort));

        // 리뷰 목록 조회 (검색어 포함)
        Page<Review> reviews;
        if (keyword != null && !keyword.isEmpty()) {
            reviews = reviewRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
        } else {
            reviews = reviewRepository.findAll(pageable);
        }

        // 결과를 ReviewDomain 객체로 변환
        List<ReviewDomain> reviewList = reviews.getContent().stream()
                .map(this::convertEntityToDomain)
                .toList();

        // ReviewListDomain 객체 반환
        return ReviewListDomain.builder()
                .pageable(PageRequest.of(reviews.getPageable().getPageNumber(),
                                reviews.getPageable().getPageSize(),
                                Sort.by(Sort.Direction.fromString(order), sort))
                )
                .reviewList(reviewList)
                .build();
    }

    @Override
    @Transactional
    public ReviewDomain createReview(ReviewDomain reviewDomain) throws Exception {
        reviewDomain.validateCreateAndUpdateReview();
        User savingUser = new User();
        savingUser.setUserNum(userUtil.getAuthenticatedUserNum());

        Review saving = new Review();
        saving.setUser(savingUser);
        saving.setTitle(reviewDomain.getTitle());
        saving.setContent(reviewDomain.getContent());
        saving.setCodeContent(reviewDomain.getCodeContent());

        Review saved = reviewRepository.save(saving);
        entityManager.refresh(saved);
        return convertEntityToDomain(saved);
    }
    @Override
    @Transactional
    public ReviewDomain deleteReview(ReviewDomain reviewDomain) throws Exception {
        Review found = reviewRepository.findById(reviewDomain.getReviewId())
                .orElseThrow(() ->new CmcException("REVIEW001"));

        if(!Objects.equals(found.getUser().getUserNum(), userUtil.getAuthenticatedUserNum())) {
            throw new CmcException("REVIEW002");
        }

        reviewRepository.deleteById(reviewDomain.getReviewId());
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

        Review saved = reviewRepository.save(found);
//        entityManager.refresh(saved);

        // 저장
        return convertEntityToDomain(saved);
    }
    // builder 대신 공통으로 활용하는 생성자
    private ReviewDomain convertEntityToDomain(Review review) {
        return ReviewDomain.builder()
                .reviewId(review.getReviewId())
                .userNum(review.getUser().getUserNum())
                .title(review.getTitle())
                .content(review.getContent())
                .codeContent(review.getCodeContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
