package com.sw.cmc.adapter.out.view.persistence;

import com.sw.cmc.entity.ReviewView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.sw.cmc.adapter.out.view.persistence
 * fileName       : ReviewViewRepository
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 리뷰 조회수 repository
 */
@Repository
public interface ReviewViewRepository extends JpaRepository<ReviewView, Long> {
}
