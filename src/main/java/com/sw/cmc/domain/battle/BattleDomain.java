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
    private Long codeOneNum;
    private String codeOneContent;
    private Long codeTwoNum;
    private String codeTwoContent;
    private Long userNum;
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
        validateEndTime();
        validateCodeOneContent();
        validateCodeTwoContent();
    }

    /**
     * methodName : validateTitle
     * author : IM HYUN WOO
     * description : battle title validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateTitle() throws CmcException {
        if (StringUtils.length(title) < 5 || StringUtils.length(content) > 60) {
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
     * description : editor code(left) validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateCodeOneContent() throws CmcException {
        if (StringUtils.length(codeOneContent) > 20000) {
            throw new CmcException("BATTLE005");
        }
    }

    /**
     * methodName : validateCodeOneContent
     * author : IM HYUN WOO
     * description : editor code(right) validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateCodeTwoContent() throws CmcException {
        if (StringUtils.length(codeTwoContent) > 20000) {
            throw new CmcException("BATTLE006");
        }
    }
}
