package com.sw.cmc.application.port.in.comment;

import com.sw.cmc.domain.comment.CommentDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.comment
 * fileName       : CommentUseCase
 * author         : ihw
 * date           : 2025. 2. 13.
 * description    : comment usecase
 */
public interface CommentUseCase {
    /**
     * methodName : createComment
     * author : IM HYUN WOO
     * description : 댓글 생성
     *
     * @param comment domain
     * @return comment domain
     * @throws Exception the exception
     */
    CommentDomain createComment(CommentDomain commentDomain) throws Exception;

    /**
     * methodName : deleteComment
     * author : IM HYUN WOO
     * description : 댓글 삭제
     *
     * @param comment domain
     * @return comment domain
     * @throws Exception the exception
     */
    CommentDomain deleteComment(CommentDomain commentDomain) throws Exception;

    /**
     * methodName : updateComment
     * author : IM HYUN WOO
     * description : 댓글 수정
     *
     * @param comment domain
     * @return comment domain
     * @throws Exception the exception
     */
    CommentDomain updateComment(CommentDomain commentDomain) throws Exception;
}
