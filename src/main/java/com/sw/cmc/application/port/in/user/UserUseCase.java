package com.sw.cmc.application.port.in.user;

import com.sw.cmc.adapter.in.user.dto.GetUserInfoResDTO;

/**
 * packageName    : com.sw.cmc.application.port.in.user
 * fileName       : UserUseCase
 * author         : SungSuHan
 * date           : 2025-02-27
 * description    :
 */
public interface UserUseCase {
    GetUserInfoResDTO getUserInfo() throws Exception;
}
