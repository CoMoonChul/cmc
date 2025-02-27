package com.sw.cmc.common.config;

import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.filter.JwtAuthenticationFilter;
import com.sw.cmc.common.jwt.JwtTokenProvider;
import com.sw.cmc.common.security.SecurityProperties;
import com.sw.cmc.common.security.CustomUserDetailsService;
import com.sw.cmc.common.util.MessageUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

/**
 * packageName    : com.sw.cmc.common.config
 * fileName       : SecurityConfig
 * author         : SungSuHan
 * date           : 2025-02-17
 * description    : Spring Security 보안 설정
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final SecurityProperties securityProperties;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF 보호 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // API 요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(securityProperties.getAuthorizationWhitelist().toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            Throwable cause = (Throwable) request.getAttribute("exception");

                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            if (cause instanceof ExpiredJwtException) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.getWriter().write("{\"errorCode\": \"TOKEN_EXPIRED\", \"message\": \"Access Token expired\"}");
                                return;
                            } else if (cause instanceof JwtException || cause instanceof IllegalArgumentException) {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                response.getWriter().write("{\"errorCode\": \"INVALID_TOKEN\", \"message\": \"Forbidden\"}");
                                return;
                            } else if (authException instanceof BadCredentialsException) {
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                response.getWriter().write("{\"errorCode\": \"INVALID_PASSWORD\", \"message\": \"비밀번호가 틀렸습니다.\"}");
                                return;
                            }

                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"errorCode\": \"SC_UNAUTHORIZED\", \"message\": \"Unauthorized\"}");
                        })
                )
                // 필터
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
