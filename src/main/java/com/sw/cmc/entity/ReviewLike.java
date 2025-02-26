package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : ReviewLike
 * author         : ihw
 * date           : 2025. 2. 17.
 * description    : review like entity
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ReviewLike {

    @EmbeddedId
    private ReviewLikeId id;

    @Column(name = "liked_at", updatable = false, insertable = false)
    private String likedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userNum")
    @JoinColumn(name = "userNum", referencedColumnName = "userNum", nullable = false)
    private User user;
}
