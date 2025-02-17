package com.sw.cmc.adapter.in.review;

import com.sw.cmc.adapter.in.review.dto.SelectReviewResDTO;
import com.sw.cmc.adapter.in.review.web.ReviewControllerApi;
import com.sw.cmc.application.port.in.review.ReviewUseCase;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.review
 * fileName       : ReviewControllerImpl
 * author         : Park Jong Il
 * date           : 25. 2. 16.
 * description    :
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
}
