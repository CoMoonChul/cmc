package com.sw.cmc.adapter.in.like;

import com.sw.cmc.adapter.in.like.dto.DeleteReviewLikeReqDTO;
import com.sw.cmc.adapter.in.like.dto.DeleteReviewLikeResDTO;
import com.sw.cmc.adapter.in.like.dto.UpdateReviewLikeReqDTO;
import com.sw.cmc.adapter.in.like.dto.UpdateReviewLikeResDTO;
import com.sw.cmc.adapter.in.like.web.LikeControllerApi;
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
    public ResponseEntity<DeleteReviewLikeResDTO> deleteReviewLike(DeleteReviewLikeReqDTO deleteReviewLikeReqDTO) throws Exception {
        LikeDomain likeDomain = LikeDomain.builder()
                .reviewId(deleteReviewLikeReqDTO.getReviewId())
                .userNum(deleteReviewLikeReqDTO.getUserNum())
                .build();
        return ResponseEntity.ok(modelMapper.map(likeUseCase.deleteReviewLike(likeDomain), DeleteReviewLikeResDTO.class));
    }

    @Override
    public ResponseEntity<UpdateReviewLikeResDTO> updateReviewLike(UpdateReviewLikeReqDTO updateReviewLikeReqDTO) throws Exception {
        LikeDomain likeDomain = LikeDomain.builder()
                .reviewId(updateReviewLikeReqDTO.getReviewId())
                .userNum(updateReviewLikeReqDTO.getUserNum())
                .build();
        return ResponseEntity.ok(modelMapper.map(likeUseCase.updateReviewLike(likeDomain), UpdateReviewLikeResDTO.class));
    }
}
