package com.sw.cmc.common.jwt;

import com.sw.cmc.domain.user.TokenDomain;
import io.jsonwebtoken.*;
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
 * description    : JWT Token 생성, 검증
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

    public TokenDomain createToken(final Claims claims) throws Exception {
        return TokenDomain.builder()
                .accessToken(createJwtToken(claims, JwtTokenType.ACCESS))
                .accessTokenExpirationTime(accessTokenExpirationTimeInSeconds)
                .refreshToken(createJwtToken(claims, JwtTokenType.REFRESH))
                .refreshTokenExpirationTime(refreshTokenExpirationTimeInSeconds)
                .build();
    }

    public String createJwtToken(final Claims claims, final JwtTokenType tokenType) throws Exception {
        final Date now = new Date();
        return Jwts.builder()
                .addClaims(claims)
                .claim("type", tokenType.name())
                .signWith(key, SignatureAlgorithm.forName("HS256"))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (tokenType == JwtTokenType.ACCESS ? accessTokenExpirationTimeInSeconds : refreshTokenExpirationTimeInSeconds)))
                .compact();
    }

    public String createAccessToken(final Claims claims) throws Exception {
        final Date now = new Date();
        return Jwts.builder()
                .addClaims(claims)
                .claim("type", JwtTokenType.ACCESS)
                .signWith(key, SignatureAlgorithm.forName("HS256"))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpirationTimeInSeconds))
                .compact();
    }

    public Claims getClaims(final String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public boolean validateToken(final String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}