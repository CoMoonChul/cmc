package com.sw.cmc.adapter.in.review.web;

import com.sw.cmc.adapter.in.review.dto.*;
import com.sw.cmc.application.port.in.review.ReviewUseCase;
import com.sw.cmc.domain.review.ReviewDomain;
import com.sw.cmc.domain.review.ReviewListDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.review
 * fileName       : ReviewControllerImpl
 * author         : Park Jong Il
 * date           : 25. 2. 16.
 * description    : Review ControllerImpl
 */
@RestController
@RequiredArgsConstructor
public class ReviewControllerImpl implements ReviewControllerApi {

    private final ModelMapper modelMapper;
    private final ReviewUseCase reviewUseCase;

    @Override
    public ResponseEntity<SelectReviewResDTO> selectReview(Long reviewId) throws Exception {
        return ResponseEntity.ok(modelMapper.map(reviewUseCase.selectReview(reviewId), SelectReviewResDTO.class));
    }

    @Override
    public ResponseEntity<SelectReviewListResDTO> selectReviewList(Integer condition, Integer pageNumber, Integer pageSize) throws Exception {
        ReviewListDomain result = reviewUseCase.selectReviewList(condition, pageNumber, pageSize);
        return ResponseEntity.ok(modelMapper.map(result, SelectReviewListResDTO.class));
    }

    @Override
    public ResponseEntity<CreateReviewResDTO> createReview(CreateReviewReqDTO createReviewReqDTO) throws Exception {
        ReviewDomain reviewDomain = ReviewDomain.builder()
                .title(createReviewReqDTO.getTitle())
                .content(createReviewReqDTO.getContent())
                .codeContent(createReviewReqDTO.getCodeContent())
                .codeType(createReviewReqDTO.getCodeType())
                .build();

        ReviewDomain createdReview = reviewUseCase.createReview(reviewDomain);
        return ResponseEntity.ok(modelMapper.map(createdReview, CreateReviewResDTO.class));
    }

    @Override
    public ResponseEntity<DeleteReviewResDTO> deleteReview(DeleteReviewReqDTO deleteReviewReqDTO) throws Exception {
        ReviewDomain reviewDomain = ReviewDomain.builder()
                .reviewId(deleteReviewReqDTO.getReviewId())
                .build();
        return ResponseEntity.ok(modelMapper.map(reviewUseCase.deleteReview(reviewDomain), DeleteReviewResDTO.class));
    }

    @Override
    public ResponseEntity<UpdateReviewResDTO> updateReview(UpdateReviewReqDTO updateReviewReqDTO) throws Exception {
        ReviewDomain reviewDomain = ReviewDomain.builder()
                .reviewId(updateReviewReqDTO.getReviewId())
                .title(updateReviewReqDTO.getTitle())
                .content(updateReviewReqDTO.getContent())
                .codeContent(updateReviewReqDTO.getCodeContent())
                .codeType(updateReviewReqDTO.getCodeType())
                .build();
        return ResponseEntity.ok(modelMapper.map(reviewUseCase.updateReview(reviewDomain), UpdateReviewResDTO.class));
    }
}
