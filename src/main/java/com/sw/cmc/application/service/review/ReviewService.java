package com.sw.cmc.application.service.review;

import com.sw.cmc.adapter.out.review.persistence.ReviewRepository;
import com.sw.cmc.application.port.in.review.ReviewUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.domain.review.ReviewDomain;
import com.sw.cmc.entity.Review;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.sw.cmc.application.service.review
 * fileName       : ReviewService
 * author         : Park Jong Il
 * date           : 25. 2. 16.
 * description    :
 */
@Service
@RequiredArgsConstructor
public class ReviewService implements ReviewUseCase {

    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;
    private final MessageUtil messageUtil;

    @Override
    public ReviewDomain selectReview(Long reviewId) throws Exception {
        Review found = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("REVIEW001")));

        return ReviewDomain.builder()
                .reviewId(found.getReviewId())
                .build();
//        return modelMapper.map(found, ReViewDomain.class);
    }

}
