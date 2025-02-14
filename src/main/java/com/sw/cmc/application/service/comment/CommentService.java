package com.sw.cmc.application.service.comment;

import com.sw.cmc.adapter.in.comment.dto.Comment;
import com.sw.cmc.adapter.in.guide.dto.Guide;
import com.sw.cmc.adapter.out.comment.persistence.CommentRepository;
import com.sw.cmc.application.port.in.comment.CommentUseCase;
import com.sw.cmc.domain.comment.CommentDomain;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.sw.cmc.application.service.comment
 * fileName       : CommentService
 * author         : ihw
 * date           : 2025. 2. 13.
 * description    : comment service
 */
@Service
@RequiredArgsConstructor
public class CommentService implements CommentUseCase {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDomain createComment(CommentDomain commentDomain) throws Exception {
        commentDomain.validateCreateComment();
        Comment saved = commentRepository.save(modelMapper.map(commentDomain, Comment.class));
        entityManager.refresh(saved);
        return CommentDomain.builder()
                .commentId(saved.getCommentId())
                .content(saved.getContent())
                .userNum(saved.getUserNum())
                .targetId(saved.getTargetId())
                .commentTarget(saved.getCommentTarget())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }
}