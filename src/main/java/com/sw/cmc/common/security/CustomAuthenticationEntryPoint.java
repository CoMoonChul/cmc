package com.sw.cmc.common.security;

import com.sw.cmc.common.util.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * packageName    : com.sw.cmc.common.security
 * fileName       : CustomAuthenticationEntryPoint
 * author         : SungSuHan
 * date           : 2025-02-20
 * description    : 인증 실패 엔트리 포인트
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final MessageUtil messageUtil;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String errorMessage; // 오류 메시지

        if (authException instanceof BadCredentialsException) {
            errorMessage = messageUtil.getFormattedMessage("USER014"); // 비밀번호가 틀렸습니다.
        } else {
            errorMessage = messageUtil.getFormattedMessage("USER013"); // 잘못된 인증 정보입니다.
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        response.setContentType("application/json;charset=UTF-8"); // JSON 형식
        response.getWriter().write(errorMessage); // 오류 메시지
    }

}
