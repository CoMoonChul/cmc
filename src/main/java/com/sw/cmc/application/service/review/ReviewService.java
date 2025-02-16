package com.sw.cmc.application.service.review;

import com.sw.cmc.adapter.in.review.dto.Review;
import com.sw.cmc.adapter.out.review.persistence.ReviewRepository;
import com.sw.cmc.application.port.in.review.ReviewUseCase;
import jakarta.persistence.EntityManager;
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
    public List<Review> selectReview(String userNum) throws Exception {
        return reviewRepository.findAllByUserNum(userNum);
    }

}
