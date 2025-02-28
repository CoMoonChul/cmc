package com.sw.cmc.application.port.in.user;

import com.sw.cmc.adapter.in.user.dto.CheckJoinResDTO;
import com.sw.cmc.adapter.in.user.dto.JoinResDTO;
import com.sw.cmc.domain.user.UserDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.user
 * fileName       : JoinUseCase
 * author         : SungSuHan
 * date           : 2025-02-11
 * description    :
 */
public interface JoinUseCase {
    UserDomain join(UserDomain userDomain) throws Exception;

    String checkUserId(String userId) throws Exception;

    String checkUsername(String username) throws Exception;
}
