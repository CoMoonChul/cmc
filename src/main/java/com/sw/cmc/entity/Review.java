package com.sw.cmc.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Column(name = "content", length = 2000, nullable = false)
    private String content;

    @Column(name = "code_content", length = 20000, nullable = false)
    private String codeContent;

    @Column(name = "code_type", length = 20, nullable = false)
    private String codeType;

    @CreationTimestamp
    @Column(name = "created_at", insertable = false, updatable = false)
    private String createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false)
    private String updatedAt;

    // User 엔티티와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_num", referencedColumnName = "userNum", nullable = false)
    private User user;
}
