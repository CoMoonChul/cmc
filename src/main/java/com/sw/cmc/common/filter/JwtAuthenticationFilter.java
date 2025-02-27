package com.sw.cmc.common.filter;

import com.sw.cmc.common.jwt.JwtAuthenticationToken;
import com.sw.cmc.common.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * packageName    : com.sw.cmc.common.filter
 * fileName       : JwtAuthenticationFilter
 * author         : SungSuHan
 * date           : 2025-02-17
 * description    : JWT Token 검증, SecurityContext 설정
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        try {
            if (Objects.nonNull(token) && jwtTokenProvider.validateToken(token)) {
                String userId = jwtTokenProvider.getClaims(token).getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                if (Objects.nonNull(userDetails)) {
                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(userDetails, token, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication); // 사용자의 정보 저장
                }
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", e);
            throw e; // 토큰 만료
        } catch (JwtException | IllegalArgumentException e) {
            request.setAttribute("exception", e);
            throw e; // 토큰 미인증
        }

        filterChain.doFilter(request, response);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (Objects.nonNull(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
