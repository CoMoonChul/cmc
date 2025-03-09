package com.sw.cmc.adapter.in.user.web;

import com.sw.cmc.adapter.in.user.dto.*;
import com.sw.cmc.application.port.in.user.LoginUseCase;
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

    @Override
    public ResponseEntity<TempLoginResDTO> tempLogin(TempLoginReqDTO tempLoginReqDTO) throws Exception {

        UserDomain userDomain = UserDomain.builder()
                .userId(tempLoginReqDTO.getUserId())
                .build();
        System.out.println("!!!!!!!!!!" + userDomain.getUserId());

        return ResponseEntity.ok(modelMapper.map(loginUseCase.tempLogin(userDomain), TempLoginResDTO.class));
    }

    @PostMapping("/user/login2")
    public ResponseEntity<LoginResDTO> login2(LoginReqDTO loginReqDTO, HttpServletResponse response) throws Exception {

        UserDomain userDomain = UserDomain.builder()
                .userId(loginReqDTO.getUserId())
                .password(loginReqDTO.getPassword())
                .build();

        LoginResDTO loginResDTO = modelMapper.map(loginUseCase.login(userDomain), LoginResDTO.class);

        // RefreshToken을 HttpOnly 쿠키로 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", loginResDTO.getRefreshToken())
                .httpOnly(true)  // JavaScript에서 접근 불가 (XSS 방어)
                .secure(false)  // HTTPS에서만 전송 (중간자 공격 방어), 개발 환경에서는 false (HTTPS가 아닐 때)
                .sameSite("None")  // 개발 "None", 상용 "Strict"
//                .path("/user/refresh")  // 특정 경로에서만 접근 가능
                .maxAge(30 * 24 * 60 * 60)  // 30일 유지
                .build();

        System.out.println("!!!!!!!!!" + refreshTokenCookie.toString());


        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        // AccessToken은 JSON 응답으로 반환 (프론트에서 Authorization 헤더로 사용)
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResDTO.getAccessToken()) // AccessToken은 헤더로 전달
                .body(loginResDTO);

    }

    @Override
    public ResponseEntity<LoginResDTO> login(LoginReqDTO loginReqDTO) throws Exception {

        UserDomain userDomain = UserDomain.builder()
                .userId(loginReqDTO.getUserId())
                .password(loginReqDTO.getPassword())
                .build();

        LoginResDTO loginResDTO = modelMapper.map(loginUseCase.login(userDomain), LoginResDTO.class);

        // RefreshToken을 HttpOnly 쿠키로 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", loginResDTO.getRefreshToken())
                .httpOnly(true)  // JavaScript에서 접근 불가 (XSS 방어)
                .secure(false)  // HTTPS에서만 전송 (중간자 공격 방어), 개발 환경에서는 false (HTTP일때도 동작하도록)
                .sameSite("Strict")  // 개발 "None", 상용 "Strict"
                .path("/user/refresh")  // 특정 경로에서만 접근 가능
                .maxAge(30 * 24 * 60 * 60)  // 30일 유지(이거 뭔지 모르겠습니다ㅎㅎ;)
                .build();

        System.out.println("!!!!!!!!!" + refreshTokenCookie.toString());

        // 쿠키 추가
        HttpServletResponse response = RequestUtil.getResponse();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        // AccessToken은 JSON 응답으로 반환 (프론트에서 Authorization 헤더로 사용)
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResDTO.getAccessToken()) // AccessToken은 헤더로 전달
                .body(loginResDTO);

    }

    @PostMapping("/user/refresh")
    public ResponseEntity<RefreshResDTO> refresh(HttpServletRequest request) throws Exception {
        // HttpOnly 쿠키에서 refreshToken 가져오기
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(401).body(new RefreshResDTO().accessToken(null));
        }

        // 새 AccessToken 발급
        String newAccessToken = loginUseCase.refresh(request);

        // ✅ 새 AccessToken을 헤더에 추가
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
