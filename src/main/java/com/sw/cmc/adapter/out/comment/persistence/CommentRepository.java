package com.sw.cmc.adapter.out.comment.persistence;

import com.sw.cmc.domain.comment.CommentVo;
import com.sw.cmc.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.sw.cmc.adapter.out.comment.persistence
 * fileName       : CommentRepository
 * author         : ihw
 * date           : 2025. 2. 13.
 * description    : comment repository
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * methodName : findByTargetIdAndCommentTarget
     * author : IM HYUN WOO
     * description : targetId와 commentId를 조회 조건으로 하여 user 테이블과 조인
     *
     * @param targetId Long
     * @param commentTarget Integer
     * @param pageable Pageable
     * @return page
     */
    @Query("SELECT new com.sw.cmc.domain.comment.CommentVo( " +
            "c, u.profileImg )" +
            "FROM Comment c " +
            "JOIN c.user u " +
            "WHERE c.targetId = :targetId AND c.commentTarget = :commentTarget")
    Page<CommentVo> findByTargetIdAndCommentTarget(@Param("targetId") Long targetId,
                                                   @Param("commentTarget") Integer commentTarget,
                                                   Pageable pageable);

    /**
     * methodName : existsByTargetIdAndCommentTarget
     * author : IM HYUN WOO
     * description : 특정 타겟의 댓글 존재 여부
     *
     * @param targetId Long
     * @param commentTarget Integer
     * @return boolean
     */
    boolean existsByTargetIdAndCommentTarget(Long targetId, Integer commentTarget);
}