package com.sw.cmc.application.service.like;

import com.sw.cmc.adapter.out.like.persistence.ReviewLikeRepository;
import com.sw.cmc.application.port.in.like.LikeUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.like.LikeDomain;
import com.sw.cmc.entity.ReviewLike;
import com.sw.cmc.entity.ReviewLikeId;
import com.sw.cmc.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.sw.cmc.application.service.like
 * fileName       : LikeService
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 좋아요 service
 */
@Service
@RequiredArgsConstructor
public class LikeService implements LikeUseCase {

    private final ReviewLikeRepository reviewLikeRepository;
    private final UserUtil userUtil;

    @Override
    public LikeDomain selectReviewLikeState(Long id) throws Exception {
        ReviewLikeId reviewLikeId = new ReviewLikeId();
        reviewLikeId.setUserNum(userUtil.getAuthenticatedUserNum());
        reviewLikeId.setReviewId(id);
        ReviewLike found = reviewLikeRepository.findById(reviewLikeId).orElse(null);
        if (found == null) {
            return LikeDomain.builder()
                    .reviewId(id)
                    .likeState(false)
                    .build();
        } else {
            return LikeDomain.builder()
                    .reviewId(id)
                    .likeState(true)
                    .build();
        }
    }

    @Override
    @Transactional
    public LikeDomain updateReviewLike(LikeDomain likeDomain) throws Exception {
        ReviewLikeId id = new ReviewLikeId();
        id.setUserNum(userUtil.getAuthenticatedUserNum());
        id.setReviewId(likeDomain.getReviewId());

        if (reviewLikeRepository.existsById(id)) {
            throw new CmcException("LIKE001");
        }

        ReviewLike saving = new ReviewLike();
        User savingUser = new User();
        savingUser.setUserNum(userUtil.getAuthenticatedUserNum());
        saving.setId(id);
        saving.setUser(savingUser);
        ReviewLike saved = reviewLikeRepository.save(saving);
        return LikeDomain.builder()
                .userNum(saved.getId().getUserNum())
                .reviewId(saved.getId().getReviewId())
                .liked_at(saved.getLikedAt())
                .build();
    }

    @Override
    @Transactional
    public LikeDomain deleteReviewLike(LikeDomain likeDomain) throws Exception {
        ReviewLikeId id = new ReviewLikeId();
        id.setReviewId(likeDomain.getReviewId());
        id.setUserNum(likeDomain.getUserNum());
        if (!reviewLikeRepository.existsById(id)) {
            throw new CmcException("LIKE002");
        }
        reviewLikeRepository.deleteById(id);
        return likeDomain;
    }
}
