package com.sw.cmc.common.util;

import java.text.ParseException;
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

    /**
     * methodName : isValidDateFormat
     * author : IM HYUN WOO
     * description : YYYYMMDD format validation
     *
     * @param date str
     * @return boolean
     */
    public static boolean isValidDateFormat(String dateStr) {
        try {
            YYYYMMDD.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * methodName : isValidDateTimeFormat
     * author : IM HYUN WOO
     * description : yyyyMMddHHmmss format validation
     *
     * @param date str
     * @return boolean
     */
    public static boolean isValidDateTimeFormat(String dateStr) {
        try {
            YMDHMS.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
