package com.sw.cmc.domain.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * packageName    : com.sw.cmc.domain.review
 * fileName       : ReviewListDomain
 * author         : Park Jong Il
 * date           : 25. 2. 20.
 * description    :
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListDomain {
    private List<ReviewDomain> reviewList;    // 리뷰 목록
    private Integer pageNumber;                // 현재 페이지 번호
    private Integer pageSize;                  // 한 페이지에 표시할 리뷰 수
    private Long totalElements;                // 전체 리뷰 개수
    private Integer totalPages;                // 전체 페이지 수
}

