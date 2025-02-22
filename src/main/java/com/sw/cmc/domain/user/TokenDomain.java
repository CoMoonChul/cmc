package com.sw.cmc.domain.user;

import lombok.Getter;
import lombok.Setter;


/**
 * packageName    : com.sw.cmc.domain
 * fileName       : TokenDomain
 * author         : SungSuHan
 * date           : 2025-02-10
 * description    : 토큰 도메인
 */
@Getter
@Setter
public class TokenDomain {
    private String accessToken;
    private long accessTokenExpirationTime;
    private String refreshToken;
    private long refreshTokenExpirationTime;
}
