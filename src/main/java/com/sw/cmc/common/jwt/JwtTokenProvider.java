package com.sw.cmc.common.jwt;

import com.sw.cmc.common.advice.CmcException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

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

    public JwtToken createToken(final Claims claims) throws Exception {
        return JwtToken.builder()
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


    // 라이브 코딩용
    public JwtToken createLcdToken(Claims claims) {
        long inviteExpirationTimeInSeconds = 60 * 60; // `1시간 유효`
        final Date now = new Date();

        String accessToken = Jwts.builder()
                .addClaims(claims)
                .claim("type", JwtTokenType.ACCESS.name()) // ✅ 토큰 타입 추가
                .signWith(key, SignatureAlgorithm.forName("HS256"))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + inviteExpirationTimeInSeconds * 1000))
                .compact();

        return JwtToken.builder()
                .accessToken(accessToken)
                .accessTokenExpirationTime(inviteExpirationTimeInSeconds)
                .build();
    }

    public UUID validateLcdToken(final String token) throws CmcException {
        try {
            // JWT 검증 및 Claims 추출
            Claims claims = jwtParser.parseClaimsJws(token).getBody();

            // ✅ 토큰 타입이 "ACCESS"인지 확인
            String type = claims.get("type", String.class);
            if (!"ACCESS".equals(type)) {
                throw new CmcException("LCD009");
            }


            String roomIdStr = claims.get("roomId", String.class);
            if (roomIdStr == null) {
                throw new CmcException("LCD0063");
            }

            return UUID.fromString(roomIdStr);
        } catch (ExpiredJwtException e) {
            throw new CmcException("LCD010");
        } catch (JwtException e) {
            throw new CmcException("LCD009");
        } catch (IllegalArgumentException e) {
            throw new CmcException("LCD011");
        }

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