package com.sw.cmc.domain.review;

import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.domain.battle.CodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * packageName    : com.sw.cmc.domain.review
 * fileName       : ReviewDomain
 * author         : Park Jong Il
 * date           : 25. 2. 18.
 * description    :
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDomain {
    private Long reviewId;
    private Long userNum;
    private String username;
    private String userImg;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
    private String codeContent;
    private String codeType;

    private Long viewCount; // 조회수
    private Long likeCount; // 좋아요 수
    /**
     * methodName : validateCreateReview
     * author : PARK JONG IL
     * description : 리뷰 등록/수정 validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateCreateAndUpdateReview() throws CmcException {
        validateTitle();
        validateContent();
        validateCodeContentLength();
        validateCodeType();
    }
    /**
     * methodName : validateCreateReview
     * author : PARK JONG IL
     * description : 리뷰 제목 0자 이상 / 50자 이내 validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateTitle() throws CmcException {
        if (StringUtils.length(title) < 1 ) {
            throw new CmcException("REVIEW003");
        }
        if (StringUtils.length(title) > 30) {
            throw new CmcException("REVIEW005");
        }
    }
    /**
     * methodName : validateCreateReview
     * author : PARK JONG IL
     * description : 리뷰 내용 미입력 / 2000자 이내 validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateContent() throws CmcException {
        if (StringUtils.length(content) < 1 ) {
            throw new CmcException("REVIEW004");
        }
        if (StringUtils.length(content) > 2000 ) {
            throw new CmcException("REVIEW006");
        }
    }
    /**
     * methodName : validateReviewCodeContent
     * author : PARK JONG IL
     * description : 리뷰 코드 컨텐츠 20000자 이내 validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateCodeContentLength() throws CmcException {
        if (StringUtils.length(codeContent) > 20000) {
            throw new CmcException("REVIEW008");
        }
    }
    /**
     * methodName : validateCodeType
     * author : PARK JONG IL
     * description : code type validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateCodeType() throws CmcException {
        if (!CodeType.isValidType(codeType)) {
            throw new CmcException("REVIEW011");
        }
    }
}
