package com.sw.cmc.adapter.out.comment.persistence;

import com.sw.cmc.adapter.in.comment.dto.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
     * @param target   id
     * @param comment  target
     * @param pageable
     * @return page
     */
    @Transactional(readOnly = true)
    @Query(value = "SELECT c FROM Comment c WHERE c.targetId = :targetId AND c.commentTarget = :commentTarget",
            countQuery = "SELECT count(c) FROM Comment c WHERE c.targetId = :targetId AND c.commentTarget = :commentTarget")
    Page<Comment> findByTargetIdAndCommentTarget(@Param("targetId") Long targetId,
                                                 @Param("commentTarget") Integer commentTarget,
                                                 Pageable pageable);
}