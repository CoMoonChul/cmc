package com.sw.cmc.adapter.in.user.web;

import com.sw.cmc.adapter.in.user.dto.GetUserInfoResDTO;
import com.sw.cmc.adapter.in.user.dto.JoinReqDTO;
import com.sw.cmc.adapter.in.user.dto.JoinResDTO;
import com.sw.cmc.application.port.in.user.UserUseCase;
import com.sw.cmc.domain.user.UserDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.user.web
 * fileName       : UserControllerImpl
 * author         : SungSuHan
 * date           : 2025-02-27
 * description    : user-controller
 */
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserControllerApi {

    private final UserUseCase userUseCase;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<GetUserInfoResDTO> getUserInfo() throws Exception {



        return ResponseEntity.ok(userUseCase.getUserInfo());
    }
}
