package com.sw.cmc.application.port.in.view;

import com.sw.cmc.domain.view.BattleViewDomain;
import com.sw.cmc.domain.view.ReviewViewDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.view
 * fileName       : ViewUseCase
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 조회수 usecase
 */
public interface ViewUseCase {
    /**
     * methodName : updateReviewView
     * author : IM HYUN WOO
     * description : 리뷰 조회수 생성 및 삭제
     *
     * @param review view domain
     * @return review view domain
     * @throws Exception the exception
     */
    ReviewViewDomain updateReviewView(ReviewViewDomain reviewViewDomain) throws Exception;

    /**
     * methodName : updateBattleView
     * author : IM HYUN WOO
     * description : 배틀 조회수 생성 및 삭제
     *
     * @param battle view domain
     * @return battle view domain
     * @throws Exception the exception
     */
    BattleViewDomain updateBattleView(BattleViewDomain battleViewDomain) throws Exception;
}
