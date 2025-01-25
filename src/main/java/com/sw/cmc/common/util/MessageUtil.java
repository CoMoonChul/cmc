package com.sw.cmc.common.util;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * packageName    : com.sw.cmc.common.util
 * fileName       : MessageUtil
 * author         : ihw
 * date           : 2025. 1. 25.
 * description    : 공통 메세지 유틸
 */
@Component
public class MessageUtil {
    private final MessageSource messageSource;

    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * methodName : getMessage
     * author : IM HYUN WOO
     * description : get common message by string code
     *
     * @param code
     * @return string
     */
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

    /**
     * methodName : getFormattedMessage
     * author : IM HYUN WOO
     * description : get formatted common message
     *
     * @param code
     * @return string
     */
    public String getFormattedMessage(String code) {
        String message = getMessage(code);
        return String.format("[%s] %s", code, message);
    }
}
