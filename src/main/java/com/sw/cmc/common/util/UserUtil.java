package com.sw.cmc.common.util;

import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * packageName    : com.sw.cmc.common.util
 * fileName       : UserUtil
 * author         : SungSuHan
 * date           : 2025-02-20
 * description    : 공통 유저 유틸
 */
@Component
@RequiredArgsConstructor
public class UserUtil {

    private final MessageUtil messageUtil;

    public Long getAuthenticatedUserNum() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional.ofNullable(authentication)
                .filter(Authentication::isAuthenticated)
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("USER012")));

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUserNum();
        }

        throw new CmcException(messageUtil.getFormattedMessage("USER013"));
    }

}
