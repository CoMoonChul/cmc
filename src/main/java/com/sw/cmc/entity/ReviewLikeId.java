package com.sw.cmc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : ReviewLikeId
 * author         : ihw
 * date           : 2025. 2. 17.
 * description    : review like 복합 키
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ReviewLikeId implements Serializable {

    @Column(name = "user_num")
    private Long userNum;

    @Column(name = "review_id")
    private Long reviewId;

    public ReviewLikeId(Long userNum, Long reviewId) {
        this.userNum = userNum;
        this.reviewId = reviewId;
    }
}
