package com.sw.cmc.adapter.in.view.web;

import com.sw.cmc.adapter.in.view.dto.UpdateBattleViewReqDTO;
import com.sw.cmc.adapter.in.view.dto.UpdateBattleViewResDTO;
import com.sw.cmc.adapter.in.view.dto.UpdateReviewViewReqDTO;
import com.sw.cmc.adapter.in.view.dto.UpdateReviewViewResDTO;
import com.sw.cmc.application.port.in.view.ViewUseCase;
import com.sw.cmc.domain.view.BattleViewDomain;
import com.sw.cmc.domain.view.ReviewViewDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.view
 * fileName       : ViewControllerImpl
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 조회수 api controller
 */
@RestController
@RequiredArgsConstructor
public class ViewControllerImpl implements ViewControllerApi {

    private final ModelMapper modelMapper;
    private final ViewUseCase viewUseCase;

    @Override
    public ResponseEntity<UpdateReviewViewResDTO> updateReviewView(UpdateReviewViewReqDTO updateReviewViewReqDTO) throws Exception {
        ReviewViewDomain reviewViewDomain = ReviewViewDomain.builder()
                .reviewId(updateReviewViewReqDTO.getReviewId())
                .viewCount(updateReviewViewReqDTO.getViewCount())
                .build();
        return ResponseEntity.ok(modelMapper.map(viewUseCase.updateReviewView(reviewViewDomain), UpdateReviewViewResDTO.class));
    }

    @Override
    public ResponseEntity<UpdateBattleViewResDTO> updateBattleView(UpdateBattleViewReqDTO updateBattleViewReqDTO) throws Exception {
        BattleViewDomain battleViewDomain = BattleViewDomain.builder()
                .battleId(updateBattleViewReqDTO.getBattleId())
                .viewCount(updateBattleViewReqDTO.getViewCount())
                .build();
        return ResponseEntity.ok(modelMapper.map(viewUseCase.updateBattleView(battleViewDomain), UpdateBattleViewResDTO.class));
    }
}
