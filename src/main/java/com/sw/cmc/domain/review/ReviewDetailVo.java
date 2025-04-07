package com.sw.cmc.domain.review;

import com.sw.cmc.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.domain.review
 * fileName       : ReviewDetailVo
 * author         : ihw
 * date           : 2025. 3. 6.
 * description    : 리뷰 상세 조회 vo
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailVo {
    private Review review;
    private String username;
    private Long userNum;
    private String userImg;
    private Long likeCount;
    private Long viewCount;
}
