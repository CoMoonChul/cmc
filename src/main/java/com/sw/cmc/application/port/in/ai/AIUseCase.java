package com.sw.cmc.application.port.in.ai;

import com.sw.cmc.domain.ai.AIDomain;

import java.io.IOException;

/**
 * packageName    : com.sw.cmc.application.port.in.ai
 * fileName       : AIUseCase
 * author         : ihw
 * date           : 2025. 4. 29.
 * description    :
 */
public interface AIUseCase {
    /**
     * methodName : createComment
     * author : IM HYUN WOO
     * description : ai 댓글 생성
     *
     * @param aiDomain AIDomain
     * @return aidomain
     */
    AIDomain createComment(AIDomain aiDomain) throws IOException;

    /**
     * methodName : deleteComment
     * author : IM HYUN WOO
     * description : ai 댓글 삭제
     *
     * @param aiDomain AIDomain
     * @return ai domain
     */
    AIDomain deleteComment(AIDomain aiDomain);


    /**
     * methodName : selectAIComment
     * author : IM HYUN WOO
     * description : ai 댓글 조회
     *
     * @param id Long
     * @return ai domain
     */
    AIDomain selectAIComment(Long id);
}
