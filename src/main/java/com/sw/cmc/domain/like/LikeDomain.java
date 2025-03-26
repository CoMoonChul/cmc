package com.sw.cmc.domain.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.domain.like
 * fileName       : LikeDomain
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    :
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeDomain {
    private Long userNum;
    private Long reviewId;
    private String liked_at;
    private boolean likeState;
}
