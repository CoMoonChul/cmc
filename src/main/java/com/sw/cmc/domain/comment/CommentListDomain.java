package com.sw.cmc.domain.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * packageName    : com.sw.cmc.domain.comment
 * fileName       : CommentListDomain
 * author         : ihw
 * date           : 2025. 2. 13.
 * description    : 댓글 리스트 도메인 객체
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentListDomain {
    private List<CommentDomain> commentList;
    private Long targetId;
    private Integer commentTarget;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
}
