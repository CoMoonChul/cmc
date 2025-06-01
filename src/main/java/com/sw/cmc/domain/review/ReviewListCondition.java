package com.sw.cmc.domain.review;

import com.sw.cmc.common.advice.CmcException;

import java.util.Arrays;

/**
 * packageName    : com.sw.cmc.domain.review
 * fileName       : ReviewListCondition
 * author         : ihw
 * date           : 2025. 6. 1.
 * description    : 리뷰 목록 검색 조건
 */
public enum ReviewListCondition {
    LATEST(0),
    MOST_LIKED(1),
    MY_REVIEW(2),
    MY_COMMENTED(3),
    MY_LIKED(4);

    private final int value;

    ReviewListCondition(int value) {
        this.value = value;
    }

    public static ReviewListCondition of(int value) {
        return Arrays.stream(values())
                .filter(c -> c.value == value)
                .findFirst()
                .orElseThrow(() -> new CmcException("REVIEW009"));
    }
}
