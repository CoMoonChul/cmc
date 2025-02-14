package com.sw.cmc.adapter.in.comment;

import com.sw.cmc.adapter.in.comment.dto.CreateCommentReqDTO;
import com.sw.cmc.adapter.in.comment.dto.CreateCommentResDTO;
import com.sw.cmc.adapter.in.comment.dto.DeleteCommentReqDTO;
import com.sw.cmc.adapter.in.comment.dto.DeleteCommentResDTO;
import com.sw.cmc.adapter.in.comment.web.CommentControllerApi;
import com.sw.cmc.application.port.in.comment.CommentUseCase;
import com.sw.cmc.domain.comment.CommentDomain;
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
    public ResponseEntity<CreateCommentResDTO> createComment(CreateCommentReqDTO createCommentReqDTO) throws Exception {
        CommentDomain commentDomain = CommentDomain.builder()
                .content(createCommentReqDTO.getContent())
                .userNum(createCommentReqDTO.getUserNum())
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
}