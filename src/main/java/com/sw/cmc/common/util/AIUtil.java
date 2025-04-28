package com.sw.cmc.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * packageName    : com.sw.cmc.common.util
 * fileName       : AIUtil
 * author         : ihw
 * date           : 2025. 4. 29.
 * description    :
 */
public class AIUtil {

    private static final Pattern EXPLANATION_PATTERN = Pattern.compile("!!!\\[설명\\]:(.*?)!!!", Pattern.DOTALL);
    private static final Pattern CODE_PATTERN = Pattern.compile("!!!\\[코드\\]:(.*?)!!!", Pattern.DOTALL);

    /**
     * OpenAI 응답에서 설명 추출
     */
    public static String extractContent(String response) {
        Matcher matcher = EXPLANATION_PATTERN.matcher(response);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    /**
     * OpenAI 응답에서 코드 추출
     */
    public static String extractCode(String response) {
        Matcher matcher = CODE_PATTERN.matcher(response);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}
