package com.sw.cmc.domain.review;

import com.sw.cmc.common.advice.CmcException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.domain.review
 * fileName       : ReviewDomain
 * author         : Park Jong Il
 * date           : 25. 2. 18.
 * description    :
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDomain {
    private Long reviewId;
    private Long userNum;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;

    public void validateCreateReview() throws CmcException {
        // 추후 추가
    }
}
