package com.sw.cmc.application.service.view;

import com.sw.cmc.adapter.out.battle.persistence.BattleRepository;
import com.sw.cmc.adapter.out.review.persistence.ReviewRepository;
import com.sw.cmc.adapter.out.view.persistence.BattleViewRepository;
import com.sw.cmc.adapter.out.view.persistence.ReviewViewRepository;
import com.sw.cmc.application.port.in.view.ViewUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.domain.view.BattleViewDomain;
import com.sw.cmc.domain.view.ReviewViewDomain;
import com.sw.cmc.entity.BattleView;
import com.sw.cmc.entity.ReviewView;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.sw.cmc.application.service.view
 * fileName       : ViewService
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    :
 */
@Service
@RequiredArgsConstructor
public class ViewService implements ViewUseCase {

    private final ModelMapper modelMapper;
    private final MessageUtil messageUtil;
    private final ReviewViewRepository reviewViewRepository;
    private final BattleViewRepository battleViewRepository;
    private final ReviewRepository reviewRepository;
    private final BattleRepository battleRepository;

    @Override
    @Transactional
    public ReviewViewDomain updateReviewView(ReviewViewDomain reviewViewDomain) throws Exception {
        if (!reviewRepository.existsById(reviewViewDomain.getReviewId())) {
            throw new CmcException(messageUtil.getFormattedMessage("VIEW001"));
        }

        ReviewView saved = reviewViewRepository.findById(reviewViewDomain.getReviewId())
                .map(existingData -> {
                    existingData.setViewCount(reviewViewDomain.getViewCount());
                    return reviewViewRepository.save(existingData);
                })
                .orElseGet(() -> reviewViewRepository.save(modelMapper.map(reviewViewDomain, ReviewView.class)));

        return ReviewViewDomain.builder()
                .reviewId(saved.getReviewId())
                .viewCount(saved.getViewCount())
                .build();
    }

    @Override
    @Transactional
    public BattleViewDomain updateBattleView(BattleViewDomain battleViewDomain) throws Exception {
        if (!battleRepository.existsById(battleViewDomain.getBattleId())) {
            throw new CmcException(messageUtil.getFormattedMessage("VIEW002"));
        }

        BattleView saved = battleViewRepository.findById(battleViewDomain.getBattleId())
                .map(existingData -> {
                    existingData.setViewCount(battleViewDomain.getViewCount());
                    return battleViewRepository.save(existingData);
                })
                .orElseGet(() -> battleViewRepository.save(modelMapper.map(battleViewDomain, BattleView.class)));

        return BattleViewDomain.builder()
                .battleId(saved.getBattleId())
                .viewCount(saved.getViewCount())
                .build();
    }
}
