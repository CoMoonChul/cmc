package com.sw.cmc.application.port.in.like;

import com.sw.cmc.domain.like.LikeDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.like
 * fileName       : LikeUseCase
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 좋아요 usecase
 */
public interface LikeUseCase {
    /**
     * methodName : updateReviewLike
     * author : IM HYUN WOO
     * description : 리뷰 좋아요 생성/수정
     *
     * @param like domain
     * @return like domain
     * @throws Exception the exception
     */
    LikeDomain updateReviewLike(LikeDomain likeDomain) throws Exception;

    /**
     * methodName : deleteReviewLike
     * author : IM HYUN WOO
     * description : 리뷰 좋아요 삭제
     *
     * @param like domain
     * @return like domain
     * @throws Exception the exception
     */
    LikeDomain deleteReviewLike(LikeDomain likeDomain) throws Exception;
}
