package com.sw.cmc.adapter.in.comment.web;

import com.sw.cmc.adapter.in.comment.dto.*;
import com.sw.cmc.application.port.in.comment.CommentUseCase;
import com.sw.cmc.domain.comment.CommentDomain;
import com.sw.cmc.domain.comment.CommentListDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.comment
 * fileName       : CommentControllerImpl
 * author         : ihw
 * date           : 2025. 2. 13.
 * description    : comment controller
 */
@RestController
@RequiredArgsConstructor
public class CommentControllerImpl implements CommentControllerApi {

    private final ModelMapper modelMapper;
    private final CommentUseCase commentUseCase;

    @Override
    public ResponseEntity<SelectCommentListResDTO> selectCommentList(Long targetId, Integer commentTarget, Integer page, Integer size) throws Exception {
        CommentListDomain result = commentUseCase.selectCommentList(targetId, commentTarget, page, size);
        return ResponseEntity.ok(modelMapper.map(result, SelectCommentListResDTO.class));
    }

    @Override
    public ResponseEntity<SelectCommentResDTO> selectComment(Long id) throws Exception {
        return ResponseEntity.ok(modelMapper.map(commentUseCase.selectComment(id), SelectCommentResDTO.class));
    }

    @Override
    public ResponseEntity<CreateCommentResDTO> createComment(CreateCommentReqDTO createCommentReqDTO) throws Exception {
        CommentDomain commentDomain = CommentDomain.builder()
                .content(createCommentReqDTO.getContent())
                .targetId(createCommentReqDTO.getTargetId())
                .commentTarget(createCommentReqDTO.getCommentTarget())
                .build();
        return ResponseEntity.ok(modelMapper.map(commentUseCase.createComment(commentDomain), CreateCommentResDTO.class));
    }

    @Override
    public ResponseEntity<DeleteCommentResDTO> deleteComment(DeleteCommentReqDTO deleteCommentReqDTO) throws Exception {
        CommentDomain commentDomain = CommentDomain.builder()
                .commentId(deleteCommentReqDTO.getCommentId())
                .build();
        return ResponseEntity.ok(modelMapper.map(commentUseCase.deleteComment(commentDomain), DeleteCommentResDTO.class));
    }

    @Override
    public ResponseEntity<UpdateCommentResDTO> updateComment(UpdateCommentReqDTO updateCommentReqDTO) throws Exception {
        CommentDomain commentDomain = CommentDomain.builder()
                .commentId(updateCommentReqDTO.getCommentId())
                .content(updateCommentReqDTO.getContent())
                .targetId(updateCommentReqDTO.getTargetId())
                .commentTarget(updateCommentReqDTO.getCommentTarget())
                .build();
        return ResponseEntity.ok(modelMapper.map(commentUseCase.updateComment(commentDomain), UpdateCommentResDTO.class));
    }
}