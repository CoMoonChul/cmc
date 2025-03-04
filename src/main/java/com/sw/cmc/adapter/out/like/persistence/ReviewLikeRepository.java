package com.sw.cmc.adapter.out.like.persistence;

import com.sw.cmc.entity.ReviewLike;
import com.sw.cmc.entity.ReviewLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.sw.cmc.adapter.out.like.persistence
 * fileName       : ReviewLikeRepository
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 리뷰 좋아요 repository
 */
@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, ReviewLikeId> {
    Long countById_ReviewId(Long reviewId);
}
