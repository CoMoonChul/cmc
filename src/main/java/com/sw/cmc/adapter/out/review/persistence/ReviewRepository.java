package com.sw.cmc.adapter.out.review.persistence;

import com.sw.cmc.adapter.in.review.dto.Review;
import com.sw.cmc.adapter.in.view.dto.BattleView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.sw.cmc.adapter.out.review.persistence
 * fileName       : ReviewRepository
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 리뷰 repository
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
