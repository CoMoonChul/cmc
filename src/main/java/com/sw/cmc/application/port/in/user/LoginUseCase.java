package com.sw.cmc.application.port.in.user;

import com.sw.cmc.domain.user.UserDomain;
import jakarta.servlet.http.HttpServletRequest;

/**
 * packageName    : com.sw.cmc.application.port.in
 * fileName       : LoginUseCase
 * author         : SungSuHan
 * date           : 2025-02-10
 * description    :
 */
public interface LoginUseCase {
    UserDomain tempLogin(UserDomain userDomain) throws Exception;

    UserDomain login(UserDomain userDomain) throws Exception;

    String refresh(HttpServletRequest request) throws Exception;

    String logout(HttpServletRequest request) throws Exception;

    String findAccount(UserDomain userDomain) throws Exception;

    String tempRefresh(String refreshToken) throws Exception;
}
