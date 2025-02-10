package com.sw.cmc.application.port.in;

import com.sw.cmc.adapter.in.user.dto.TempLoginResponse;
import com.sw.cmc.domain.TempLogin;

/**
 * packageName    : com.sw.cmc.application.port.in
 * fileName       : LoginUseCase
 * author         : SungSuHan
 * date           : 2025-02-10
 * description    :
 */
public interface LoginUseCase {
    TempLoginResponse tempLogin(TempLogin tempLogin) throws Exception;
}
