package com.sw.cmc.adapter.out.review.persistence;

import com.sw.cmc.domain.review.ReviewDetailVo;
import com.sw.cmc.domain.review.ReviewListVo;
import com.sw.cmc.entity.Review;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * packageName    : com.sw.cmc.adapter.out.review.persistence
 * fileName       : ReviewRepository
 * author         : Park Jong Il
 * date           : 2025. 2. 16.
 * description    : 리뷰 repository
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * methodName : findReviewDetail
     * author : IM HYUN WOO
     * description : 리뷰 상세 조회
     *
     * @param reviewId Long
     * @return battle detail vo
     */
    @Query("SELECT new com.sw.cmc.domain.review.ReviewDetailVo( " +
            "r, u.username, u.userNum, u.profileImg," +
            "(SELECT COUNT(*) FROM ReviewLike rl WHERE r.reviewId = rl.id.reviewId), " +
            "(SELECT rv.viewCount FROM ReviewView rv WHERE rv.reviewId = r.reviewId) ) " +
            "FROM Review r " +
            "LEFT JOIN r.user u " +
            "WHERE r.reviewId = :reviewId")
    Optional<ReviewDetailVo> findReviewDetail(@Param("reviewId") Long reviewId);


    /**
     * methodName : findAllReviews
     * author : IM HYUN WOO
     * description : 리뷰 전체 조회
     *
     * @param pageable Pageable
     * @return page
     */
    @Query("SELECT new com.sw.cmc.domain.review.ReviewListVo( " +
            "r, u.username, u.profileImg," +
            "(SELECT COUNT(*) FROM ReviewLike rl WHERE rl.id.reviewId = r.reviewId), " +
            "(SELECT rv.viewCount FROM ReviewView rv WHERE rv.reviewId = r.reviewId) )" +
            "FROM Review r " +
            "LEFT JOIN r.user u")
    Page<ReviewListVo> findAllReviews(Pageable pageable);


    /**
     * methodName : findAllOrderByLikeCountDesc
     * author : IM HYUN WOO
     * description : 좋아요 많은순
     *
     * @param pageable Pageable
     * @return page
     */
    @Query("SELECT new com.sw.cmc.domain.review.ReviewListVo( " +
            "r, u.username, u.profileImg, " +
            "COUNT(rl.id.reviewId), " +
            "(SELECT rv.viewCount FROM ReviewView rv WHERE rv.reviewId = r.reviewId) )" +
            "FROM Review r " +
            "JOIN r.user u " +
            "LEFT JOIN ReviewLike rl ON rl.id.reviewId = r.reviewId " +
            "GROUP BY r.reviewId, u.username, u.profileImg " +
            "ORDER BY COUNT(rl.id.reviewId) DESC")
    Page<ReviewListVo> findAllOrderByLikeCountDesc(Pageable pageable);

    /**
     * methodName : findMyReviews
     * author : IM HYUN WOO
     * description : 본인이 작성한 리뷰
     *
     * @param userNum Long
     * @param pageable Pageable
     * @return page
     */
    @Query("SELECT new com.sw.cmc.domain.review.ReviewListVo( " +
            "r, u.username, u.profileImg, " +
            "COUNT(rl), rv.viewCount) " +   // COUNT 대신 rl 개수로
            "FROM Review r " +
            "JOIN r.user u " +
            "LEFT JOIN ReviewLike rl ON rl.id.reviewId = r.reviewId " +
            "LEFT JOIN ReviewView rv ON rv.reviewId = r.reviewId " +
            "WHERE r.user.userNum = :userNum " +
            "GROUP BY r, u.username, u.profileImg, rv.viewCount")
    Page<ReviewListVo> findMyReviews(Long userNum, Pageable pageable);

    /**
     * methodName : findMyCommentingReviews
     * author : IM HYUN WOO
     * description : 본인이 댓글단 리뷰
     *
     * @param userNum Long
     * @param pageable Pageable
     * @return page
     */
    @Query("SELECT new com.sw.cmc.domain.review.ReviewListVo( " +
            "r, u.username, u.profileImg," +
            "(SELECT COUNT(*) FROM ReviewLike rl WHERE rl.id.reviewId = r.reviewId), " +
            "(SELECT rv.viewCount FROM ReviewView rv WHERE rv.reviewId = r.reviewId) )" +
            "FROM Review r " +
            "JOIN Comment c ON c.targetId = r.reviewId AND c.commentTarget = 0 " +
            "LEFT JOIN r.user u " +
            "WHERE c.user.userNum = :userNum")
    Page<ReviewListVo> findMyCommentingReviews(Long userNum, Pageable pageable);

    /**
     * methodName : findMyLikeReviews
     * author : IM HYUN WOO
     * description : 본인이 좋아요한 리뷰
     *
     * @param userNum Long
     * @param pageable Pageable
     * @return page
     */
    @Query("SELECT new com.sw.cmc.domain.review.ReviewListVo( " +
            "r, u.username, u.profileImg," +
            "(SELECT COUNT(*) FROM ReviewLike rl WHERE rl.id.reviewId = r.reviewId), " +
            "(SELECT rv.viewCount FROM ReviewView rv WHERE rv.reviewId = r.reviewId) ) " +
            "FROM Review r " +
            "JOIN ReviewLike rl ON rl.id.reviewId = r.reviewId " +
            "JOIN r.user u " +
            "WHERE rl.user.userNum = :userNum " +
            "GROUP BY r")
    Page<ReviewListVo> findMyLikeReviews(Long userNum, Pageable pageable);
}
