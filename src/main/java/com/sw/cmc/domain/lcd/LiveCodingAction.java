package com.sw.cmc.domain.lcd;

import lombok.Getter;

/**
 * packageName    : com.sw.cmc.domain.lcd
 * fileName       : LiveCodingAction
 * author         : Ko
 * date           : 2025-03-06
 * description    :
 */
@Getter
public enum LiveCodingAction {
    JOIN(0),
    LEAVE(1),
    DELETE(2);

    private final int action;

    LiveCodingAction(int action) {
        this.action = action;
    }

}
