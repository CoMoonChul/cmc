package com.sw.cmc.adapter.in.user;

import com.sw.cmc.adapter.in.user.dto.TempLoginRequest;
import com.sw.cmc.adapter.in.user.dto.TempLoginResponse;
import com.sw.cmc.adapter.in.user.web.LoginControllerApi;
import com.sw.cmc.application.port.in.user.LoginUseCase;
import com.sw.cmc.domain.user.TempLogin;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in
 * fileName       : LoginControllerImpl
 * author         : SungSuHan
 * date           : 2025-02-07
 * description    : 로그인 api controller
 */
@RestController
@RequiredArgsConstructor
public class LoginControllerImpl implements LoginControllerApi {

    private final LoginUseCase loginUseCase;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<TempLoginResponse> tempLogin(TempLoginRequest tempLoginRequest) throws Exception {
        return ResponseEntity.ok(loginUseCase.tempLogin(modelMapper.map(tempLoginRequest, TempLogin.class)));
    }
}
