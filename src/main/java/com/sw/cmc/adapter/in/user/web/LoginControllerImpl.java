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

        UserDomain userDomain = UserDomain.builder()
                .userId(tempLoginReqDTO.getUserId())
                .build();
        System.out.println("!!!!!!!!!!" + userDomain.getUserId());

        return ResponseEntity.ok(modelMapper.map(loginUseCase.tempLogin(userDomain), TempLoginResDTO.class));
    }

    @Override
    public ResponseEntity<LoginResDTO> login(LoginReqDTO loginReqDTO) throws Exception {

        UserDomain userDomain = UserDomain.builder()
                .userId(loginReqDTO.getUserId())
                .password(loginReqDTO.getPassword())
                .build();

        return ResponseEntity.ok(modelMapper.map(loginUseCase.login(userDomain), LoginResDTO.class));
    }

    @PostMapping("/user/refresh")
    public ResponseEntity<RefreshResDTO> refresh(HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(new RefreshResDTO().accessToken(loginUseCase.refresh(request)));
    }

    @PostMapping("/user/logout")
    public ResponseEntity<LogoutResDTO> logout(HttpServletRequest request) throws Exception {
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
