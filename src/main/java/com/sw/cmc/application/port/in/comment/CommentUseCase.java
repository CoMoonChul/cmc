package com.sw.cmc.application.port.in.comment;

import com.sw.cmc.domain.comment.CommentDomain;
import com.sw.cmc.domain.comment.CommentListDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.comment
 * fileName       : CommentUseCase
 * author         : ihw
 * date           : 2025. 2. 13.
 * description    : comment usecase
 */
public interface CommentUseCase {

    /**
     * methodName : selectComment
     * author : IM HYUN WOO
     * description : 댓글 단건 조회
     *
     * @param id
     * @return comment domain
     * @throws Exception the exception
     */
    CommentDomain selectComment(Long id) throws Exception;

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

    /**
     * methodName : selectCommentList
     * author : IM HYUN WOO
     * description : targetId, commentTarget 기반 댓글 다건 조회
     *
     * @param target  id
     * @param comment target
     * @param page
     * @param size
     * @return comment list domain
     * @throws Exception the exception
     */
    CommentListDomain selectCommentList(Long targetId, Integer commentTarget, Integer page, Integer size) throws Exception;
}
