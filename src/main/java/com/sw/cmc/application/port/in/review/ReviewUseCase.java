package com.sw.cmc.application.port.in.review;

import com.sw.cmc.domain.comment.CommentDomain;
import com.sw.cmc.domain.review.ReViewDomain;
import com.sw.cmc.entity.Review;

import java.util.List;

/**
 * packageName    : com.sw.cmc.application.port.in.review
 * fileName       : ReviewUseCase
 * author         : Park Jong Il
 * date           : 25. 2. 16.
 * description    :
 */
public interface ReviewUseCase {
    /**
     * methodName : selectReview
     * author : Park Jong Il
     * description :
     *
     * @return review domain
     * @throws Exception the exception
     */
    ReViewDomain selectReview(Long reviewId) throws Exception;
}
