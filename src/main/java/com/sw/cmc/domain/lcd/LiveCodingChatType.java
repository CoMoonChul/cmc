package com.sw.cmc.domain.lcd;

import lombok.Getter;

/**
 * packageName    : com.sw.cmc.domain.lcd
 * fileName       : LiveCodingAction
 * author         : Ko
 * date           : 2025-03-06
 * description    : LiveCodingChatType
 */
@Getter
public enum LiveCodingChatType {
    IN_OUT(0),
    CHAT(1);

    private final int type;

    LiveCodingChatType(int type) {
        this.type = type;
    }

}
