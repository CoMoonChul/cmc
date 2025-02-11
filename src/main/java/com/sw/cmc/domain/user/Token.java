package com.sw.cmc.domain.user;

import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.sw.cmc.domain
 * fileName       : Token
 * author         : SungSuHan
 * date           : 2025-02-10
 * description    :
 */
@Getter
@Builder
public class Token {
    private String accessToken;
    private long accessTokenExpirationTime;
    private String refreshToken;
    private long refreshTokenExpirationTime;
}
