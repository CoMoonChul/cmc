package com.sw.cmc.common.util;

import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.jwt.JwtProperties;
import com.sw.cmc.common.jwt.JwtToken;
import com.sw.cmc.common.jwt.JwtTokenProvider;
import com.sw.cmc.common.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Optional;


/**
 * packageName    : com.sw.cmc.common.util
 * fileName       : UserUtil
 * author         : SungSuHan
 * date           : 2025-02-20
 * description    : 공통 유저 유틸
 */
@Component
@RequiredArgsConstructor
public class UserUtil {

    private final MessageUtil messageUtil;
    private final JwtProperties properties;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String ALGORITHM = "AES";

    public Long getAuthenticatedUserNum() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUserNum();
        }

        throw new CmcException(messageUtil.getFormattedMessage("USER013"));
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public JwtToken createToken(final long userNum, final String userId) throws Exception {
        final Claims claims = Jwts.claims();

        claims.setSubject(userId);
        claims.put("userNum", userNum);

        return jwtTokenProvider.createToken(claims);
    }

    public String createAccessToken(final long userNum, final String userId) throws Exception {
        final Claims claims = Jwts.claims();

        claims.setSubject(userId);
        claims.put("userNum", userNum);

        return jwtTokenProvider.createAccessToken(claims);
    }

    // RefreshToken 암호화
    public String encrypt(String data) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(properties.getToken().getEncryptionKey()); // Base64 디코딩
        SecretKeySpec key = new SecretKeySpec(decodedKey, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData); // Base64로 인코딩하여 저장
    }

    // RefreshToken 복호화
    public String decrypt(String encryptedData) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(properties.getToken().getEncryptionKey()); // Base64 디코딩
        SecretKeySpec key = new SecretKeySpec(decodedKey, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }

}
