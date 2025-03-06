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
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private List<ReviewDomain> reviewList;



}

