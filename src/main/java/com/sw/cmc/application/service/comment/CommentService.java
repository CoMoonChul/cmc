package com.sw.cmc.application.service.comment;


import com.sw.cmc.adapter.out.battle.persistence.BattleRepository;
import com.sw.cmc.adapter.out.comment.persistence.CommentRepository;
import com.sw.cmc.adapter.out.review.persistence.ReviewRepository;
import com.sw.cmc.application.port.in.comment.CommentUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.comment.CommentDomain;
import com.sw.cmc.domain.comment.CommentListDomain;
import com.sw.cmc.domain.comment.CommentTarget;
import com.sw.cmc.domain.comment.CommentVo;
import com.sw.cmc.entity.Comment;
import com.sw.cmc.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    private final ReviewRepository reviewRepository;
    private final BattleRepository battleRepository;
    private final UserUtil userUtil;

    @Override
    public CommentDomain selectComment(Long id) throws Exception {
        Comment found = commentRepository.findById(id)
                .orElseThrow(() -> new CmcException("COMMENT001"));
        return modelMapper.map(found, CommentDomain.class);
    }

    @Override
    public CommentListDomain selectCommentList(Long targetId, Integer commentTarget, Integer page, Integer size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CommentVo> res = commentRepository.findByTargetIdAndCommentTarget(targetId, commentTarget, pageable);
        List<CommentDomain> list = res.getContent().stream().map(e -> {
            return CommentDomain.builder()
                    .commentId(e.getComment().getCommentId())
                    .content(e.getComment().getContent())
                    .userNum(e.getComment().getUser().getUserNum())
                    .targetId(e.getComment().getTargetId())
                    .commentTarget(e.getComment().getCommentTarget())
                    .createdAt(e.getComment().getCreatedAt())
                    .updatedAt(e.getComment().getUpdatedAt())
                    .userName(e.getComment().getUser().getUsername())
                    .userImg(e.getUserImg())
                    .build();
        }).toList();

        return CommentListDomain.builder()
                .pageNumber(res.getPageable().getPageNumber())
                .pageSize(res.getPageable().getPageSize())
                .totalElements(res.getTotalElements())
                .totalPages(res.getTotalPages())
                .commentList(list)
                .build();
    }

    @Override
    @Transactional
    public CommentDomain createComment(CommentDomain commentDomain) throws Exception {
        commentDomain.validateCreateComment();

        CommentTarget target = CommentTarget.fromCode(commentDomain.getCommentTarget());
        if (target.isReview() && !reviewRepository.existsById(commentDomain.getTargetId())) {
            throw new CmcException("COMMENT004");
        } else if (target.isBattle() && !battleRepository.existsById(commentDomain.getTargetId())) {
            throw new CmcException("COMMENT006");
        }

        User savingUser = new User();
        savingUser.setUserNum(userUtil.getAuthenticatedUserNum());
        Comment saving = modelMapper.map(commentDomain, Comment.class);
        saving.setUser(savingUser);
        Comment saved = commentRepository.save(saving);
        entityManager.refresh(saved);
        return modelMapper.map(saved, CommentDomain.class);
    }

    @Override
    @Transactional
    public CommentDomain deleteComment(CommentDomain commentDomain) throws Exception {
        Comment found = commentRepository.findById(commentDomain.getCommentId())
                .orElseThrow(() -> new CmcException("COMMENT001"));

        if (!Objects.equals(found.getUser().getUserNum(), userUtil.getAuthenticatedUserNum())) {
            throw new CmcException("COMMENT005");
        }

        commentRepository.deleteById(commentDomain.getCommentId());
        return commentDomain;
    }

    @Override
    @Transactional
    public CommentDomain updateComment(CommentDomain commentDomain) throws Exception {
        commentDomain.validateUpdateComment();
        Comment found = commentRepository.findById(commentDomain.getCommentId())
                .orElseThrow(() -> new CmcException("COMMENT001"));

        CommentTarget target = CommentTarget.fromCode(commentDomain.getCommentTarget());
        if (target.isReview() && !reviewRepository.existsById(commentDomain.getTargetId())) {
            throw new CmcException("COMMENT004");
        } else if (target.isBattle() && !battleRepository.existsById(commentDomain.getTargetId())) {
            throw new CmcException("COMMENT006");
        }

        if (!Objects.equals(found.getUser().getUserNum(), userUtil.getAuthenticatedUserNum())) {
            throw new CmcException("COMMENT005");
        }

        found.setContent(commentDomain.getContent());
        found.setTargetId(commentDomain.getTargetId());
        found.setCommentTarget(commentDomain.getCommentTarget());
        return modelMapper.map(commentRepository.save(found), CommentDomain.class);
    }
}
