package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;
//
//    @Column(name = "user_num", nullable = false)
//    private Long userNum;

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
    @JoinColumn(name = "user_num", referencedColumnName = "userNum", nullable = false)
    private User user;

}
