package com.sw.cmc.domain.user;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import com.sw.cmc.common.advice.CmcException;

import java.time.LocalDateTime;
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
public class Join {
    private String userId;
    private String password;
    private String userName;
    private String email;
    private LocalDateTime createdDtm;
    private LocalDateTime updatedDtm;

    private String resultMessage;

    public void validateUserId(String userId, String message) {
        if (StringUtils.length(userId) < 4 || StringUtils.length(userId) > 15) {
            throw new CmcException(message);
        }
    }
    public void validatePassword(String password, String message) {
        final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).{6,}$";
        if (!Pattern.matches(PASSWORD_REGEX, password)) {
            throw new CmcException(message);
        }
    }
    public void validateEmail(String email, String message) {
        final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (StringUtils.isNotEmpty(email) && !Pattern.matches(EMAIL_REGEX, email)) {
            throw new CmcException(message);
        }
    }
    public void validateUserName(String userName, String message) {
        if (StringUtils.length(userName) < 2 || StringUtils.length(userName) > 20) {
            throw new CmcException(message);
        }
    }
}
