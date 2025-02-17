package com.sw.cmc.application.service.comment;

import com.sw.cmc.adapter.in.comment.dto.Comment;
import com.sw.cmc.adapter.out.comment.persistence.CommentRepository;
import com.sw.cmc.application.port.in.comment.CommentUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.domain.comment.CommentDomain;
import com.sw.cmc.domain.comment.CommentListDomain;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final MessageUtil messageUtil;

    @Override
    public CommentDomain selectComment(Long id) throws Exception {
        Comment found = commentRepository.findById(id)
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("COMMENT001")));
        return CommentDomain.builder()
                .commentId(found.getCommentId())
                .content(found.getContent())
                .userNum(found.getUserNum())
                .targetId(found.getTargetId())
                .commentTarget(found.getCommentTarget())
                .createdAt(found.getCreatedAt())
                .updatedAt(found.getUpdatedAt())
                .build();
    }

    @Override
    public CommentListDomain selectCommentList(Long targetId, Integer commentTarget, Integer page, Integer size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> res =  commentRepository.findByTargetIdAndCommentTarget(targetId, commentTarget, pageable);
        List<CommentDomain> commentDomains = res.getContent().stream()
                .map(this::convertToCommentDomain)
                .toList();

        return CommentListDomain.builder()
                .pageNumber(res.getPageable().getPageNumber())
                .pageSize(res.getPageable().getPageSize())
                .totalElements(res.getTotalElements())
                .totalPages(res.getTotalPages())
                .commentList(commentDomains)
                .build();
    }

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

    @Override
    @Transactional
    public CommentDomain deleteComment(CommentDomain commentDomain) throws Exception {
        commentDomain.validateDeleteComment();
        commentRepository.deleteById(commentDomain.getCommentId());
        return commentDomain;
    }

    @Override
    @Transactional
    public CommentDomain updateComment(CommentDomain commentDomain) throws Exception {
        commentDomain.validateUpdateComment();
        Comment found = commentRepository.findById(commentDomain.getCommentId())
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("COMMENT001")));
        found.setContent(commentDomain.getContent());
        found.setTargetId(commentDomain.getTargetId());
        found.setCommentTarget(commentDomain.getCommentTarget());
        Comment saved = commentRepository.save(found);
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

    private CommentDomain convertToCommentDomain(Comment comment) {
        return new CommentDomain(
                comment.getCommentId(),
                comment.getContent(),
                comment.getUserNum(),
                comment.getTargetId(),
                comment.getCommentTarget(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
