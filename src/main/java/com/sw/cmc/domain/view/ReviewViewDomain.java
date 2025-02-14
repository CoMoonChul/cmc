package com.sw.cmc.domain.view;

import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.sw.cmc.domain.view
 * fileName       : ReviewViewDomain
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 리뷰 조회수 도메인
 */
@Getter
@Builder
public class ReviewViewDomain {
    private Long reviewId;
    private Long viewCount;
}
