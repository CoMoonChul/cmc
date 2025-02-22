package com.sw.cmc.domain.comment;

import com.sw.cmc.common.advice.CmcException;

/**
 * packageName    : com.sw.cmc.domain.comment
 * fileName       : CommentTarget
 * author         : ihw
 * date           : 2025. 2. 22.
 * description    : comment target enum
 */
public enum CommentTarget {
    REVIEW(0),
    BATTLE(1);

    private final int code;

    CommentTarget(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CommentTarget fromCode(int code) {
        for (CommentTarget target : CommentTarget.values()) {
            if (target.getCode() == code) {
                return target;
            }
        }
        throw new CmcException("COMMENT003");
    }

    public static boolean isValidCode(int code) {
        for (CommentTarget target : CommentTarget.values()) {
            if (target.getCode() == code) {
                return true;
            }
        }
        return false;
    }
}