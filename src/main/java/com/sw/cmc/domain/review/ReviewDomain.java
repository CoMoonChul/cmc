package com.sw.cmc.domain.review;

import com.sw.cmc.common.advice.CmcException;
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
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;

    // Editor 관련 필드 추가
    private Long codeEditNum;  // code_edit_data의 code_edit_num
    private String editorContent;  // code_edit_data의 content
    private String editorLanguage;  // code_edit_data의 language

    /**
     * methodName : validateCreateReview
     * author : PARK JONG IL
     * description : 리뷰 등록/수정 validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateCreateAndUpdateReview() throws CmcException {
        validateTitle();
        validateTitleMaxLength();
        validateContent();
        validateContentMaxLength();
    }
    /**
     * methodName : validateCreateReview
     * author : PARK JONG IL
     * description : 리뷰 제목 0자 이상 validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateTitle() throws CmcException {
        if (StringUtils.length(title) < 1 ) {
            throw new CmcException("REVIEW003");
        }
    }
    /**
     * methodName : validateCreateReview
     * author : PARK JONG IL
     * description : 리뷰 제목 50자 이내 validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateTitleMaxLength() throws CmcException {
        if (StringUtils.length(title) > 30) {
            throw new CmcException("REVIEW005");
        }
    }

    public void validateContent() throws CmcException {
        if (StringUtils.length(content) < 1 ) {
            throw new CmcException("REVIEW004");
        }
    }
    /**
     * methodName : validateCreateReview
     * author : PARK JONG IL
     * description : 리뷰 내용 2000자 이내 validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateContentMaxLength() throws CmcException {
        if (StringUtils.length(content) > 2000 ) {
            throw new CmcException("REVIEW006");
        }
    }
}
