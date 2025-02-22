package com.sw.cmc.common.jwt;

import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.sw.cmc.common.jwt
 * fileName       : JwtToken
 * author         : SungSuHan
 * date           : 2025-02-23
 * description    :
 */
@Getter
@Builder
public class JwtToken {
    private String accessToken;
    private long accessTokenExpirationTime;
    private String refreshToken;
    private long refreshTokenExpirationTime;
}
