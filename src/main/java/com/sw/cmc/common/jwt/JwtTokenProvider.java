package com.sw.cmc.common.jwt;

import com.sw.cmc.common.config.JwtProperties;
import com.sw.cmc.domain.user.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * packageName    : com.sw.cmc.common.jwt
 * fileName       : JwtTokenProvider
 * author         : SungSuHan
 * date           : 2025-02-10
 * description    :
 */
@Component
public class JwtTokenProvider {

    private final Key key;
    private final JwtParser jwtParser;

    public final long accessTokenExpirationTimeInSeconds;
    public final long refreshTokenExpirationTimeInSeconds;

    public JwtTokenProvider(final JwtProperties properties) {
        key = Keys.hmacShaKeyFor(properties.getToken().getSecretKey().getBytes(StandardCharsets.UTF_8));
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        accessTokenExpirationTimeInSeconds = 1_000 * properties.getToken().getAccessTokenExpirationTimeInSeconds();
        refreshTokenExpirationTimeInSeconds = 1_000 * properties.getToken().getRefreshTokenExpirationTimeInSeconds();
    }

    /**
     * methodName : createToken
     * author : SungSuHan
     * description :
     *
     * @param claims
     * @return token
     */
    public Token createToken(final Claims claims) {
        return Token.builder()
                .accessToken(createToken(claims, JwtTokenType.ACCESS))
                .accessTokenExpirationTime(accessTokenExpirationTimeInSeconds)
                .refreshToken(createToken(claims, JwtTokenType.REFRESH))
                .refreshTokenExpirationTime(refreshTokenExpirationTimeInSeconds)
                .build();
    }

    /**
     * methodName : createToken
     * author : SungSuHan
     * description :
     *
     * @param claims
     * @param tokenType
     * @return string
     */
    public String createToken(final Claims claims, final JwtTokenType tokenType) {
        final Date now = new Date();
        return Jwts.builder()
                .addClaims(claims)
                .claim("type", tokenType.name())
                .signWith(key, SignatureAlgorithm.forName("HS256"))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (tokenType == JwtTokenType.ACCESS ? accessTokenExpirationTimeInSeconds : refreshTokenExpirationTimeInSeconds)))
                .compact();
    }

}
