package com.sw.cmc.adapter.out.review.persistence;

import com.sw.cmc.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
//    List<Review> findAllByUserNum(String userNum);
    /**
     * 제목 또는 내용에 특정 키워드가 포함된 리뷰 검색
     * @param titleKeyword 검색할 제목 키워드
     * @param contentKeyword 검색할 내용 키워드
     * @param pageable 페이징 정보
     * @return 검색된 리뷰 목록 (페이징)
     */
    Page<Review> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword, Pageable pageable);
}
