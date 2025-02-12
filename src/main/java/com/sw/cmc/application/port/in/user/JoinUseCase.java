package com.sw.cmc.application.port.in.user;

import com.sw.cmc.adapter.in.user.dto.JoinCheckResponse;
import com.sw.cmc.adapter.in.user.dto.JoinResponse;
import com.sw.cmc.domain.user.Join;

/**
 * packageName    : com.sw.cmc.application.port.in.user
 * fileName       : JoinUseCase
 * author         : SungSuHan
 * date           : 2025-02-11
 * description    :
 */
public interface JoinUseCase {
    JoinResponse join(Join join) throws Exception;

    JoinCheckResponse checkUserId(String userId) throws Exception;

    JoinCheckResponse checkUserName(String userName) throws Exception;
}
