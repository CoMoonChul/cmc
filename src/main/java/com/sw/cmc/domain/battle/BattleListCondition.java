package com.sw.cmc.domain.battle;

import com.sw.cmc.common.advice.CmcException;

import java.util.Arrays;

/**
 * packageName    : com.sw.cmc.domain.battle
 * fileName       : BattleListCondition
 * author         : ihw
 * date           : 2025. 5. 31.
 * description    : 배틀 검색 조건 enum
 */
public enum BattleListCondition {
    LATEST(0),
    MOST_VOTED(1),
    MY_BATTLE(2),
    MY_VOTE(3);

    private final int value;

    BattleListCondition(int value) {
        this.value = value;
    }

    public static BattleListCondition of(int value) {
        return Arrays.stream(values())
                .filter(c -> c.value == value)
                .findFirst()
                .orElseThrow(() -> new CmcException("BATTLE010"));
    }
}
