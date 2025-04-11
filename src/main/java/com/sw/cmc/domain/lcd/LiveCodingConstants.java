package com.sw.cmc.domain.lcd;

import com.sw.cmc.common.advice.CmcException;

/**
 * packageName    : com.sw.cmc.domain.lcd
 * fileName       : LiveCodingConstants
 * author         : Ko
 * date           : 2025-04-06
 * description    : LiveCodingConstants
 */
public class LiveCodingConstants {

    private LiveCodingConstants() {
        throw new CmcException("LCD018");
    }

    // 라이브코딩방 redis key
    public static final String LCD_PREFIX = "live_coding:";
    // 라이브코드 redis key
    public static final String LCD_CODE_PREFIX = LCD_PREFIX + "code:";

}