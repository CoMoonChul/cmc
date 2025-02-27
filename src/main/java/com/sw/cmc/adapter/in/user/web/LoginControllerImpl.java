package com.sw.cmc.adapter.in.user.web;

import com.sw.cmc.adapter.in.user.dto.*;
import com.sw.cmc.application.port.in.user.LoginUseCase;
import com.sw.cmc.domain.user.UserDomain;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
        return ResponseEntity.ok(loginUseCase.tempLogin(modelMapper.map(tempLoginReqDTO, UserDomain.class)));
    }

    @Override
    public ResponseEntity<LoginResDTO> login(LoginReqDTO loginReqDTO) throws Exception {
        return ResponseEntity.ok(loginUseCase.login(modelMapper.map(loginReqDTO, UserDomain.class)));
    }

    @PostMapping("/user/refresh")
    public ResponseEntity<RefreshResDTO> refresh(HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(loginUseCase.refresh(request));
    }

    @PostMapping("/user/logout")
    public ResponseEntity<LogoutResDTO> logout(HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(loginUseCase.logout(request));
    }

    @Override
    public ResponseEntity<FindAccountResDTO> findAccount(FindAccountReqDTO findAccountReqDTO) throws Exception {
        return ResponseEntity.ok(loginUseCase.findAccount(modelMapper.map(findAccountReqDTO, UserDomain.class)));
    }
}
