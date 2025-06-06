package com.sw.cmc.adapter.in.user.web;

import com.sw.cmc.adapter.in.user.dto.*;
import com.sw.cmc.application.port.in.user.LoginUseCase;
import com.sw.cmc.common.jwt.JwtProperties;
import com.sw.cmc.common.util.RequestUtil;
import com.sw.cmc.domain.user.UserDomain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in
 * fileName       : LoginControllerImpl
 * author         : SungSuHan
 * date           : 2025-02-07
 * description    : login-controller
 */
@RestController
@RequiredArgsConstructor
public class LoginControllerImpl implements LoginControllerApi {

    private final LoginUseCase loginUseCase;
    private final ModelMapper modelMapper;
    private final JwtProperties properties;

    @Override
    public ResponseEntity<TempLoginResDTO> tempLogin(TempLoginReqDTO tempLoginReqDTO) throws Exception {

        UserDomain userDomain = UserDomain.builder()
                .userId(tempLoginReqDTO.getUserId())
                .build();
        return ResponseEntity.ok(modelMapper.map(loginUseCase.tempLogin(userDomain), TempLoginResDTO.class));
    }

    @Override
    public ResponseEntity<LoginResDTO> login(LoginReqDTO loginReqDTO) throws Exception {

        UserDomain userDomain = UserDomain.builder()
                .userId(loginReqDTO.getUserId())
                .password(loginReqDTO.getPassword())
                .build();

        LoginResDTO loginResDTO = modelMapper.map(loginUseCase.login(userDomain), LoginResDTO.class);

        // refresh token set cookie
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", loginResDTO.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(properties.getToken().getRefreshTokenExpirationTimeInSeconds()) // 90 days
                .build();

        HttpServletResponse response = RequestUtil.getResponse();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResDTO.getAccessToken()) // access token
                .body(loginResDTO);
    }

    @Override
    public ResponseEntity<LoginGoogleResDTO> loginGoogle(LoginGoogleReqDTO loginGoogleReqDTO) throws Exception {
        UserDomain userDomain = UserDomain.builder()
                .idToken(loginGoogleReqDTO.getIdToken())
                .build();

        LoginGoogleResDTO loginResDTO = modelMapper.map(loginUseCase.loginGoogle(userDomain), LoginGoogleResDTO.class);
        // refresh token set cookie
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", loginResDTO.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(properties.getToken().getRefreshTokenExpirationTimeInSeconds()) // 90 days
                .build();

        HttpServletResponse response = RequestUtil.getResponse();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResDTO.getAccessToken()) // access token
                .body(loginResDTO);
    }

    @PostMapping("/user/refresh")
    public ResponseEntity<RefreshResDTO> refresh(HttpServletRequest request) throws Exception {
        // 새로운 AccessToken 발급
        String newAccessToken = loginUseCase.refresh(request);

        // 새로운 AccessToken 헤더에 추가
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
                .body(new RefreshResDTO().accessToken(newAccessToken));
    }

    @Override
    public ResponseEntity<RefreshResDTO> tempRefresh(RefreshReqDTO refreshReqDTO) throws Exception {
        // 새로운 AccessToken 발급
        String newAccessToken = loginUseCase.tempRefresh(refreshReqDTO.getRefreshToken());

        // 새로운 AccessToken 헤더에 추가
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)
                .body(new RefreshResDTO().accessToken(newAccessToken));
    }

    @PostMapping("/user/logout")
    public ResponseEntity<LogoutResDTO> logout(HttpServletRequest request) throws Exception {
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/user/refresh")
                .maxAge(0) // 즉시 만료
                .build();

        HttpServletResponse response = RequestUtil.getResponse();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok(new LogoutResDTO().resultMessage(loginUseCase.logout(request)));
    }

    @Override
    public ResponseEntity<FindAccountResDTO> findAccount(FindAccountReqDTO findAccountReqDTO) throws Exception {

        UserDomain userDomain = UserDomain.builder()
                .email(findAccountReqDTO.getEmail())
                .build();

        return ResponseEntity.ok(new FindAccountResDTO().resultMessage(loginUseCase.findAccount(userDomain)));
    }
}
