package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : Review
 * author         : ihw
 * date           : 2025. 2. 17.
 * description    : review entity
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "user_num", nullable = false)
    private Long userNum;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "content", length = 5000, nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private String createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private String updatedAt;

    // User 엔티티와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_num", insertable = false, updatable = false)
    private User user;
//
//    // 좋아요 관계 설정
//    @OneToMany(mappedBy = "review")
//    private Set<ReviewLike> likes = new HashSet<>();
//
//    // 댓글 관계 설정 (리뷰에 달린 댓글만 조회)
//    @OneToMany
//    @JoinColumn(name = "target_id")
//    @Where(clause = "comment_target = 1") // 1은 리뷰 댓글을 의미한다고 가정
//    private List<Comment> comments = new ArrayList<>();
//
//    // 좋아요 수 계산 (DB 레벨에서 계산)
//    @Formula("(SELECT COUNT(*) FROM review_like rl WHERE rl.review_id = review_id)")
//    private Long likeCount;
//
//    // 댓글 수 계산 (DB 레벨에서 계산)
//    @Formula("(SELECT COUNT(*) FROM comment c WHERE c.target_id = review_id AND c.comment_target = 1)")
//    private Long commentCount;

    // 비즈니스 메서드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
//    }
//
//    public boolean hasUserLiked(Long userNum) {
//        return likes.stream()
//                .anyMatch(like -> like.getUser().getUserNum().equals(userNum));
//    }
//
//    public boolean hasUserCommented(Long userNum) {
//        return comments.stream()
//                .anyMatch(comment -> comment.getUser().getUserNum().equals(userNum));
    }

    // 생성자
    public Review(Long userNum, String title, String content) {
        this.userNum = userNum;
        this.title = title;
        this.content = content;
    }
}
