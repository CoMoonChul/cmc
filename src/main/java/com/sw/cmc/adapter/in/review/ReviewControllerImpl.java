package com.sw.cmc.adapter.in.review;

import com.sw.cmc.adapter.in.review.dto.SelectReviewListResDTO;
import com.sw.cmc.adapter.in.review.dto.SelectReviewResDTO;
import com.sw.cmc.adapter.in.review.web.ReviewControllerApi;
import com.sw.cmc.application.port.in.review.ReviewUseCase;
import com.sw.cmc.domain.comment.CommentListDomain;
import com.sw.cmc.domain.review.ReviewListDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;

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

    @Override
    public ResponseEntity<SelectReviewListResDTO> selectReviewList(
            Integer page,
            Integer size,
            String sort,
            String order,
            String keyword
    ) throws Exception {
        ReviewListDomain result = reviewUseCase.selectReviewList(page, size, sort, order, keyword);
        return ResponseEntity.ok(modelMapper.map(result, SelectReviewListResDTO.class));
    }
}
