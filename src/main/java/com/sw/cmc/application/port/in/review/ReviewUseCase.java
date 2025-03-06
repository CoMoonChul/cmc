package com.sw.cmc.application.port.in.review;

import com.sw.cmc.domain.review.ReviewDomain;
import com.sw.cmc.domain.review.ReviewListDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.review
 * fileName       : ReviewUseCase
 * author         : Park Jong Il
 * date           : 25. 2. 16.
 * description    : review UseCase
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
     * @param condition Integer
     * @param pageNumber Integer
     * @param pageSize Integer
     * @return review list domain
     * @throws Exception the exception
     */
    ReviewListDomain selectReviewList(Integer condition, Integer pageNumber, Integer pageSize)throws Exception;

    /**
     * methodName : createReview
     * author : Park Jong Il
     * description : 리뷰 생성
     *
     * @param reviewDomain : title, content, codeContent
     * @throws Exception the exception
     */
    ReviewDomain createReview(ReviewDomain reviewDomain) throws Exception;
    /**
     * methodName : deleteReview
     * author : Park Jong Il
     * description : 리뷰 삭제
     *
     * @param reviewDomain : reviewId
     * @throws Exception the exception
     */
    ReviewDomain deleteReview(ReviewDomain reviewDomain) throws Exception;
    /**
     * methodName : updateReview
     * author : Park Jong Il
     * description : 리뷰 수정
     *
     * @param reviewDomain : reviewId, title, content, codeContent
     * @throws Exception the exception
     */
    ReviewDomain updateReview(ReviewDomain reviewDomain) throws Exception;
}
