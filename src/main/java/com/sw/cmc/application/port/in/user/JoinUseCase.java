package com.sw.cmc.application.port.in.user;

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
}
