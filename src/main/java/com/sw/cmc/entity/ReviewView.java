package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : ReviewView
 * author         : ihw
 * date           : 2025. 2. 17.
 * description    : review view entity
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ReviewView {

    @Id
    private Long reviewId;

    private Long viewCount;

}
