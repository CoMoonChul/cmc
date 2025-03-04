package com.sw.cmc.domain.battle;

import com.sw.cmc.common.advice.CmcException;
import lombok.Getter;

/**
 * packageName    : com.sw.cmc.domain.battle
 * fileName       : VoteValue
 * author         : ihw
 * date           : 2025. 3. 1.
 * description    : vote value enum
 */
@Getter
public enum VoteValue {
    LEFT(0),
    RIGHT(1);

    private final int code;

    VoteValue(int code) {
        this.code = code;
    }

    public static VoteValue fromCode(int code) {
        for (VoteValue target : VoteValue.values()) {
            if (target.getCode() == code) {
                return target;
            }
        }
        throw new CmcException("BATTLE009");
    }

    public static boolean isValidCode(int code) {
        for (VoteValue target : VoteValue.values()) {
            if (target.getCode() == code) {
                return true;
            }
        }
        return false;
    }

    /**
     * methodName : isLeft
     * author : IM HYUN WOO
     * description : left 코드 여부
     *
     * @return boolean
     */
    public boolean isLeft() {
        return this == LEFT;
    }

    /**
     * methodName : isRight
     * author : IM HYUN WOO
     * description : right 코드 여부
     *
     * @return boolean
     */
    public boolean isRight() {
        return this == RIGHT;
    }
}
