package com.sw.cmc.domain.battle;

import com.sw.cmc.common.advice.CmcException;
import lombok.Getter;

import java.util.Objects;

/**
 * packageName    : com.sw.cmc.domain.battle
 * fileName       : CodeType
 * author         : ihw
 * date           : 2025. 3. 20.
 * description    : code type enum
 */
@Getter
public enum CodeType {
    JAVA("java"),
    JAVASCRIPT("javascript"),
    PYTHON("python");

    private final String type;

    CodeType(String type) {
        this.type = type;
    }

    public static CodeType fromCode(String type) {
        for (CodeType target : CodeType.values()) {
            if (Objects.equals(target.getType(), type)) {
                return target;
            }
        }
        throw new CmcException("BATTLE012");
    }

    public static boolean isValidType(String type) {
        for (CodeType target : CodeType.values()) {
            if (Objects.equals(target.getType(), type)) {
                return true;
            }
        }
        return false;
    }
}
