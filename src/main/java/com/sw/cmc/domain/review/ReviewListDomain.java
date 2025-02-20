package com.sw.cmc.domain.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

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
    //    private Integer pageNumber;                // 현재 페이지 번호
    //    private Integer pageSize;                  // 한 페이지에 표시할 리뷰 수
    //    private string sort;                       // 분류
    private Pageable pageable; // 페이징 처리 obj pageable
    private List<ReviewDomain> reviewList;    // 리뷰 목록



}

