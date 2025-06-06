package com.sw.cmc.domain.battle;

import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * packageName    : com.sw.cmc.domain.battle
 * fileName       : BattleDomain
 * author         : ihw
 * date           : 2025. 2. 28.
 * description    : Battle domain
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BattleDomain {
    private Long battleId;
    private String title;
    private String content;
    private String endTime;
    private String codeContentLeft;
    private String codeContentRight;
    private String codeTypeLeft;
    private String codeTypeRight;
    private Long leftVote;
    private Long rightVote;
    private Long userNum;
    private String username;
    private String userImg;
    private Long viewCount;
    private Integer voteValue;
    private String createdAt;
    private String updatedAt;

    /**
     * methodName : validateCreateBattle
     * author : IM HYUN WOO
     * description : battle 생성 validation
     */
    public void validateCreateBattle() throws CmcException {
        validateTitle();
        validateContent();
        validateCodeContentLeft();
        validateCodeContentRight();
        validateCodeType();
    }

    /**
     * methodName : validateUpdateBattle
     * author : IM HYUN WOO
     * description : battle 수정 validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateUpdateBattle() throws CmcException {
        validateTitle();
        validateContent();
        validateCodeContentLeft();
        validateCodeContentRight();
        validateCodeType();
    }

    /**
     * methodName : validateTitle
     * author : IM HYUN WOO
     * description : battle title validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateTitle() throws CmcException {
        if (StringUtils.length(title) < 5 || StringUtils.length(title) > 60) {
            throw new CmcException("BATTLE002");
        }
    }

    /**
     * methodName : validateContent
     * author : IM HYUN WOO
     * description : battle content validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateContent() throws CmcException {
        if (StringUtils.length(content) > 1200) {
            throw new CmcException("BATTLE003");
        }
    }

    /**
     * methodName : validateEndTime
     * author : IM HYUN WOO
     * description : battle endtime yyyymmddhhmmss format validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateEndTime() throws CmcException {
        // validate when endTime exist
        if (StringUtils.isNotBlank(endTime) && !DateUtil.isValidDateTimeFormat(endTime)) {
            throw new CmcException("BATTLE004");
        }
    }

    /**
     * methodName : validateCodeOneContent
     * author : IM HYUN WOO
     * description : codeContentLeft validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateCodeContentLeft() throws CmcException {
        if (StringUtils.length(codeContentLeft) > 20000 || StringUtils.length(codeContentLeft) < 1) {
            throw new CmcException("BATTLE005");
        }
    }

    /**
     * methodName : validateCodeOneContent
     * author : IM HYUN WOO
     * description : codeContentRight validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateCodeContentRight() throws CmcException {
        if (StringUtils.length(codeContentRight) > 20000 || StringUtils.length(codeContentRight) < 1) {
            throw new CmcException("BATTLE006");
        }
    }

    /**
     * methodName : validateCodeType
     * author : IM HYUN WOO
     * description : code type validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateCodeType() throws CmcException {
        if (!CodeType.isValidType(codeTypeLeft) || !CodeType.isValidType(codeTypeRight)) {
            throw new CmcException("BATTLE012");
        }
    }
}
