package com.sw.cmc.common.util;

import com.sw.cmc.common.jwt.JwtTokenProvider;
import com.sw.cmc.domain.user.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.sw.cmc.common.util
 * fileName       : LoginUtil
 * author         : SungSuHan
 * date           : 2025-02-10
 * description    : 공통 로그인 유틸
 */
@Component
@RequiredArgsConstructor
public class LoginUtil {

    private final JwtTokenProvider jwtTokenProvider;

    public Token createToken(final long userNum, final String userId) throws Exception {

        final Claims claims = Jwts.claims();
        claims.setSubject(userId);
        claims.put("userNum", userNum);

        return jwtTokenProvider.createToken(claims);

    }

}
