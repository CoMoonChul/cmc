package com.sw.cmc.domain.comment;

import com.sw.cmc.common.advice.CmcException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String userName;
    private String email;

    public void validateCreateComment() throws CmcException {
        validateContent();
    }

    public void validateDeleteComment() throws CmcException {

    }

    public void validateUpdateComment() throws CmcException {

    }

    private void validateContent() throws CmcException {
    }
}
