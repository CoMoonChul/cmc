package com.sw.cmc.domain.battle;

import com.sw.cmc.common.advice.CmcException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.domain.battle
 * fileName       : BattleVoteDomain
 * author         : ihw
 * date           : 2025. 3. 1.
 * description    : 배틀 투표 도메인
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BattleVoteDomain {
    private Long battleId;
    private Integer voteValue;

    /**
     * methodName : validateUpdateBattleVote
     * author : IM HYUN WOO
     * description : battle vote update validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateUpdateBattleVote() throws CmcException {
        validateVoteValue();
    }

    /**
     * methodName : validateVoteValue
     * author : IM HYUN WOO
     * description : vote value validation
     *
     * @throws CmcException the cmc exception
     */
    public void validateVoteValue() throws CmcException {
        if (!VoteValue.isValidCode(voteValue)) {
            throw new CmcException("BATTLE009");
        }
    }
}
