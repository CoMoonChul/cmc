package com.sw.cmc.adapter.in.like.web;

import com.sw.cmc.adapter.in.like.dto.*;
import com.sw.cmc.application.port.in.like.LikeUseCase;
import com.sw.cmc.domain.like.LikeDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.like
 * fileName       : LikeControllerImpl
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 좋아요 controller
 */
@RestController
@RequiredArgsConstructor
public class LikeControllerImpl implements LikeControllerApi {

    private final LikeUseCase likeUseCase;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<SelectReviewLikeStateResDTO> selectReviewLikeState(Long id) throws Exception {
        return ResponseEntity.ok(modelMapper.map(likeUseCase.selectReviewLikeState(id), SelectReviewLikeStateResDTO.class));
    }

    @Override
    public ResponseEntity<DeleteReviewLikeResDTO> deleteReviewLike(DeleteReviewLikeReqDTO deleteReviewLikeReqDTO) throws Exception {
        LikeDomain likeDomain = LikeDomain.builder()
                .reviewId(deleteReviewLikeReqDTO.getReviewId())
                .build();
        return ResponseEntity.ok(modelMapper.map(likeUseCase.deleteReviewLike(likeDomain), DeleteReviewLikeResDTO.class));
    }

    @Override
    public ResponseEntity<UpdateReviewLikeResDTO> updateReviewLike(UpdateReviewLikeReqDTO updateReviewLikeReqDTO) throws Exception {
        LikeDomain likeDomain = LikeDomain.builder()
                .reviewId(updateReviewLikeReqDTO.getReviewId())
                .build();
        return ResponseEntity.ok(modelMapper.map(likeUseCase.updateReviewLike(likeDomain), UpdateReviewLikeResDTO.class));
    }
}
