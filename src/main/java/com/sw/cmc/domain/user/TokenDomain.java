package com.sw.cmc.domain.user;

import com.sw.cmc.common.util.UserUtil;
import lombok.Builder;
import lombok.Getter;


/**
 * packageName    : com.sw.cmc.domain
 * fileName       : TokenDomain
 * author         : SungSuHan
 * date           : 2025-02-10
 * description    : 토큰 도메인
 */
@Getter
@Builder
public class TokenDomain {
    private String accessToken;
    private long accessTokenExpirationTime;
    private String refreshToken;
    private long refreshTokenExpirationTime;

    public static TokenDomain encryptToken(TokenDomain tokenDomain, UserUtil userUtil) throws Exception {
        return TokenDomain.builder()
                .accessToken(userUtil.encrypt(tokenDomain.getAccessToken()))
                .accessTokenExpirationTime(tokenDomain.getAccessTokenExpirationTime())
                .refreshToken(userUtil.encrypt(tokenDomain.getRefreshToken()))
                .refreshTokenExpirationTime(tokenDomain.getRefreshTokenExpirationTime())
                .build();
    }

}
