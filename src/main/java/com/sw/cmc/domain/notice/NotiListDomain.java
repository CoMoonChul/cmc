package com.sw.cmc.domain.notice;

import lombok.*;

import java.util.List;

/**
 * packageName    : com.sw.cmc.domain.notice
 * fileName       : NotiListDomain
 * author         : dkstm
 * date           : 2025-02-17
 * description    :
 */
@Getter
@Setter
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
