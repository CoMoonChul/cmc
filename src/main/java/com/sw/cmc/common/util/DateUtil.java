package com.sw.cmc.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * packageName    : com.sw.cmc.common.util
 * fileName       : DateUtil
 * author         : ihw
 * date           : 2025. 1. 25.
 * description    : 공통 날짜 유틸
 */
public class DateUtil {
    public final static SimpleDateFormat YMDHMS = new SimpleDateFormat("yyyyMMddHHmmss");
    public final static SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");


    /**
     * methodName : getTodayDateTimeString
     * author : IM HYUN WOO
     * description : get today formatted yyyyMMddHHmmss
     *
     * @return string
     */
    public static String getTodayDateTimeString() {
        Calendar today = Calendar.getInstance();
        return YMDHMS.format(today.getTime());
    }

    /**
     * methodName : getTodayDateString
     * author : IM HYUN WOO
     * description : get today formatted yyyyMMdd
     *
     * @return string
     */
    public static String getTodayDateString() {
        Calendar today =  Calendar.getInstance();
        return YYYYMMDD.format(today.getTime());
    }
}
