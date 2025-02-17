package com.sw.cmc.adapter.out.review.persistence;

import com.sw.cmc.adapter.in.review.dto.Review;
import com.sw.cmc.adapter.in.view.dto.BattleView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.sw.cmc.adapter.out.review.persistence
 * fileName       : ReviewRepository
 * author         : Park Jong Il
 * date           : 2025. 2. 16.
 * description    : 리뷰 repository
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUserNum(String userNum);
}
