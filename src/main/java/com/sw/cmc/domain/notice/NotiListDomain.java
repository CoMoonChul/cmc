package com.sw.cmc.domain.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * packageName    : com.sw.cmc.domain.notice
 * fileName       : NotiListDomain
 * author         : dkstm
 * date           : 2025-02-17
 * description    :
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotiListDomain {
    private List<NoticeDomain> notiList;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;

}
