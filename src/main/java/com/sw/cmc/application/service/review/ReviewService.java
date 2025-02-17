package com.sw.cmc.application.service.review;

import com.sw.cmc.adapter.out.review.persistence.ReviewRepository;
import com.sw.cmc.application.port.in.review.ReviewUseCase;
import com.sw.cmc.domain.comment.CommentDomain;
import com.sw.cmc.domain.review.ReViewDomain;
import com.sw.cmc.entity.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public ReViewDomain selectReview(Long reviewId) throws Exception {
        Review found = reviewRepository.findById(reviewId)
         .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));

        // Entity를 Domain으로 변환
        return null;
//        return modelMapper.map(found, ReViewDomain.class);
    }

}
