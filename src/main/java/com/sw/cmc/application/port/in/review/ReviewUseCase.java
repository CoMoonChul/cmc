package com.sw.cmc.application.port.in.review;

import com.sw.cmc.domain.review.ReviewDomain;
import com.sw.cmc.domain.review.ReviewListDomain;
import org.hibernate.query.Page;

import java.awt.print.Pageable;

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
     * description : 리뷰 단건 조회
     *
     * @return review domain
     * @throws Exception the exception
     */
    ReviewDomain selectReview(Long reviewId) throws Exception;

    /**
     * methodName : selectReviewList
     * author : Park Jong Il
     * description : 리뷰 리스트 조회
     *
     * @param page
     * @param size
     * @param sort
     * @param order
     * @param keyword
     * @return review list domain
     * @throws Exception the exception
     */
    ReviewListDomain selectReviewList(
            Integer page,
            Integer size,
            String sort,
            String order,
            String keyword
    ) throws Exception;
//
//    void toggleReviewLike(Long reviewId) throws Exception;
//
//    boolean checkReviewLike(Long reviewId) throws Exception;
//
//    long getReviewLikeCount(Long reviewId) throws Exception;
//
//    long getReviewCommentCount(Long reviewId) throws Exception;
    /**
     * methodName : selectReviewList
     * author : Park Jong Il
     * description : 리뷰 생성
     *
     * @param review Domain
     * @throws Exception the exception
     */
    ReviewDomain createReview(ReviewDomain reviewDomain) throws Exception;
}
