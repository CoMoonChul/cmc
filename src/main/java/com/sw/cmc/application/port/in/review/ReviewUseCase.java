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
     * @param page : 페이지
     * @param size : 페이지 사이즈
     * @param sort : 정렬
     * @param order : 내림차순, 오름차순 정렬
     * @param keyword : 검색어
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
     * methodName : createReview
     * author : Park Jong Il
     * description : 리뷰 생성
     *
     * @param reviewDomain : title, content
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
     * description : 리뷰 삭제
     *
     * @param reviewDomain : reviewId, title, content
     * @throws Exception the exception
     */
    ReviewDomain updateReview(ReviewDomain reviewDomain) throws Exception;
}
