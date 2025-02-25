package com.sw.cmc.adapter.in.review;

import com.sw.cmc.adapter.in.review.dto.CreateReviewReqDTO;
import com.sw.cmc.adapter.in.review.dto.CreateReviewResDTO;
import com.sw.cmc.adapter.in.review.dto.SelectReviewListResDTO;
import com.sw.cmc.adapter.in.review.dto.SelectReviewResDTO;
import com.sw.cmc.adapter.in.review.web.ReviewControllerApi;
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

    @Override
    public ResponseEntity<CreateReviewResDTO> createReview(CreateReviewReqDTO createReviewReqDTO) throws Exception {
        ReviewDomain reviewDomain = ReviewDomain.builder()
                .title(createReviewReqDTO.getTitle())
                .content(createReviewReqDTO.getContent())
                .build();
//        return ResponseEntity.ok(modelMapper.map(reviewUseCase.createReview(reviewDomain), CreateReviewResDTO.class));
        ReviewDomain createdReview = reviewUseCase.createReview(reviewDomain);

        // 응답 DTO 생성 및 메시지 설정
        CreateReviewResDTO response = modelMapper.map(createdReview, CreateReviewResDTO.class);
        response.setResultMessage("리뷰가 정상적으로 생성되었습니다.");

        return ResponseEntity.ok(response);
    }
//
//    @Override
//    public ResponseEntity<DeleteReviewResDTO> deleteReview(DeleteReviewReqDTO deleteReviewReqDTO) throws Exception {
//        ReviewDomain reviewDomain = ReviewDomain.builder()
//                .reviewId(deleteReviewReqDTO.getReview_id())
//                .userNum(deleteReviewReqDTO.getUser_num())
//                .build();
//        return ResponseEntity.ok(modelMapper.map(reviewUseCase.deleteReview(reviewDomain), DeleteReviewResDTO.class));
//    }
//
//    @Override
//    public ResponseEntity<UpdateReviewResDTO> updateReview(UpdateReviewReqDTO updateReviewReqDTO) throws Exception {
//        ReviewDomain reviewDomain = ReviewDomain.builder()
//                .reviewId(updateReviewReqDTO.getReview_id())
//                .userNum(updateReviewReqDTO.getUser_num())
//                .title(updateReviewReqDTO.getTitle())
//                .content(updateReviewReqDTO.getContent())
//                .build();
//        return ResponseEntity.ok(modelMapper.map(reviewUseCase.updateReview(reviewDomain), UpdateReviewResDTO.class));
//    }
}
