package com.sw.cmc.common.util;

import com.sw.cmc.common.constant.Constants;

/**
 * packageName    : com.sw.cmc.common.util
 * fileName       : HeaderUtil
 * author         : ihw
 * date           : 2025. 1. 25.
 * description    : 공통 헤더 유틸
 */
public class HeaderUtil {
    /**
     * methodName : getHeader
     * author : IM HYUN WOO
     * description : getHeader content by string key
     *
     * @param key
     * @return string
     */
    public static String getHeader(String key) {
        return RequestUtil.getRequest().getHeader(key);
    }

    /**
     * methodName : getXUserId
     * author : IM HYUN WOO
     * description : get header content X-User-Id
     *
     * @return string
     */
    public static String getXUserId() {
        return RequestUtil.getRequest().getHeader(Constants.HEADER_KEY_USER_ID);
    }

    /**
     * methodName : getXUserNum
     * author : IM HYUN WOO
     * description : get header content X-User-Num
     *
     * @return string
     */
    public static String getXUserNum() {
        return RequestUtil.getRequest().getHeader(Constants.HEADER_KEY_USER_NUM);
    }

    /**
     * methodName : getXTimestamp
     * author : IM HYUN WOO
     * description : get header content X-Timestamp
     *
     * @return string
     */
    public static String getXTimestamp() {
        return RequestUtil.getRequest().getHeader(Constants.HEADER_KEY_TIMESTAMP);
    }
}
