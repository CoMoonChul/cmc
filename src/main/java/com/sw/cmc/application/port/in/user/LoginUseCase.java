package com.sw.cmc.application.port.in.user;

import com.sw.cmc.adapter.in.user.dto.LoginResDTO;
import com.sw.cmc.adapter.in.user.dto.TempLoginResDTO;
import com.sw.cmc.domain.user.LoginDomain;
import com.sw.cmc.domain.user.TempLoginDomain;

/**
 * packageName    : com.sw.cmc.application.port.in
 * fileName       : LoginUseCase
 * author         : SungSuHan
 * date           : 2025-02-10
 * description    :
 */
public interface LoginUseCase {
    TempLoginResDTO tempLogin(TempLoginDomain tempLoginDomain) throws Exception;

    LoginResDTO login(LoginDomain loginDomain) throws Exception;
}
