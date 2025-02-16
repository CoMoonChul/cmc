package com.sw.cmc.adapter.out.comment.persistence;

import com.sw.cmc.adapter.in.comment.dto.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
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
}