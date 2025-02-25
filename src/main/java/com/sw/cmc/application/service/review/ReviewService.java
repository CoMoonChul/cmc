package com.sw.cmc.application.service.review;

import com.sw.cmc.adapter.in.review.dto.DeleteReviewReqDTO;
import com.sw.cmc.adapter.out.review.persistence.ReviewRepository;
import com.sw.cmc.application.port.in.review.ReviewUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.review.ReviewDomain;
import com.sw.cmc.domain.review.ReviewListDomain;
import com.sw.cmc.entity.Review;
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
    private final UserUtil userUtil;

    @Override
    public ReviewDomain selectReview(Long reviewId) throws Exception {
        Review found = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CmcException("REVIEW001"));

        return ReviewDomain.builder()
                .reviewId(found.getReviewId())
                .userNum(found.getUser().getUserNum())
                .title(found.getTitle())
                .content(found.getContent())
                .createdAt(found.getCreatedAt())
                .updatedAt(found.getUpdatedAt())
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
        reviewDomain.validateCreateReview();

        User savingUser = new User();
        savingUser.setUserNum(userUtil.getAuthenticatedUserNum());
        Review saving = modelMapper.map(reviewDomain, Review.class);
        saving.setUser(savingUser);
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
    private ReviewDomain convertEntityToDomain(Review review) {
        return new ReviewDomain(
            review.getReviewId(),
            review.getUser().getUserNum(),
            review.getTitle(),
            review.getContent(),
            review.getCreatedAt(),
            review.getUpdatedAt()
        );
    }
}
