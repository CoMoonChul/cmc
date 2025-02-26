package com.sw.cmc.application.port.in.user;

import com.sw.cmc.adapter.in.user.dto.LoginResDTO;
import com.sw.cmc.adapter.in.user.dto.LogoutResDTO;
import com.sw.cmc.adapter.in.user.dto.RefreshResDTO;
import com.sw.cmc.adapter.in.user.dto.TempLoginResDTO;
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
    TempLoginResDTO tempLogin(UserDomain userDomain) throws Exception;

    LoginResDTO login(UserDomain userDomain) throws Exception;

    RefreshResDTO refresh(HttpServletRequest request) throws Exception;

    LogoutResDTO logout(HttpServletRequest request) throws Exception;
}
