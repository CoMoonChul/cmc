package com.sw.cmc.domain.user;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import com.sw.cmc.common.advice.CmcException;

import java.util.regex.Pattern;

/**
 * packageName    : com.sw.cmc.domain.user
 * fileName       : Join
 * author         : SungSuHan
 * date           : 2025-02-11
 * description    :
 */
@Getter
@Setter
public class JoinDomain {
    private String userId;
    private String password;
    private String username;
    private String email;

    private String resultMessage;

    public static void validateUserId(String userId, String message) {
        if (StringUtils.length(userId) < 4 || StringUtils.length(userId) > 15) {
            throw new CmcException(message);
        }
    }
    public static void validatePassword(String password, String message) {
        final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).{6,}$";
        if (!Pattern.matches(PASSWORD_REGEX, password)) {
            throw new CmcException(message);
        }
    }
    public static void validateEmail(String email, String message) {
        final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (StringUtils.isNotEmpty(email) && !Pattern.matches(EMAIL_REGEX, email)) {
            throw new CmcException(message);
        }
    }
    public static void validateUsername(String username, String message) {
        if (StringUtils.length(username) < 2 || StringUtils.length(username) > 20) {
            throw new CmcException(message);
        }
    }
}
