package com.sw.cmc.domain.comment;

import lombok.*;

/**
 * packageName    : com.sw.cmc.domain.comment
 * fileName       : CommentDomain
 * author         : ihw
 * date           : 2025. 2. 13.
 * description    : 댓글 도메인 객체
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDomain {
    private Long commentId;
    private String content;
    private Long userNum;
    private Long targetId;
    private Integer commentTarget;
    private String createdAt;
    private String updatedAt;

    public void validateCreateComment() throws Exception {

    }

    public void validateDeleteComment() throws Exception {

    }

    public void validateUpdateComment() throws Exception {

    }
}
