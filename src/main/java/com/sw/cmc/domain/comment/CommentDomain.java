package com.sw.cmc.domain.comment;

import com.sw.cmc.common.advice.CmcException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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
    private String userImg;

    /**
     * methodName : validateCreateComment
     * author : IM HYUN WOO
     * description : 댓글 생성 validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateCreateComment() throws CmcException {
        validateContent();
        validateCommentTarget();
    }

    /**
     * methodName : validateUpdateComment
     * author : IM HYUN WOO
     * description : 댓글 수정 validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateUpdateComment() throws CmcException {
        validateContent();
        validateCommentTarget();
    }

    /**
     * methodName : validateContent
     * author : IM HYUN WOO
     * description : 댓글 content validation (5에서 500자)
     *
     * @throws CmcException the cmc exception
     */
    public void validateContent() throws CmcException {
        if (StringUtils.length(content) < 2 || StringUtils.length(content) > 500) {
            throw new CmcException("COMMENT002");
        }
    }

    /**
     * methodName : validateCommentTarget
     * author : IM HYUN WOO
     * description : 댓글 comment target validation (0 or 1 by enum)
     *
     * @throws CmcException the cmc exception
     */
    public void validateCommentTarget() throws CmcException {
        if (!CommentTarget.isValidCode(commentTarget)) {
            throw new CmcException("COMMENT003");
        }
    }
}
