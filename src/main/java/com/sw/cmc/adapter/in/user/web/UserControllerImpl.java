package com.sw.cmc.adapter.in.user.web;

import com.sw.cmc.adapter.in.user.dto.*;
import com.sw.cmc.application.port.in.user.UserUseCase;
import com.sw.cmc.common.util.RequestUtil;
import com.sw.cmc.domain.user.UserDomain;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    public ResponseEntity<GetMyInfoResDTO> getMyInfo() throws Exception {
        return ResponseEntity.ok(modelMapper.map(userUseCase.getMyInfo(), GetMyInfoResDTO.class));
    }

    @Override
    public ResponseEntity<WithdrawResDTO> withdraw(WithdrawReqDTO withdrawReqDTO) throws Exception {

        UserDomain userDomain = UserDomain.builder()
                .userId(withdrawReqDTO.getUserId())
                .build();

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0) // 즉시 만료
                .build();
        HttpServletResponse response = RequestUtil.getResponse();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        return ResponseEntity.ok(new WithdrawResDTO().resultMessage(userUseCase.withdraw(userDomain)));
    }

    @Override
    public ResponseEntity<UpdateResDTO> update(UpdateReqDTO updateReqDTO) throws Exception {
        UserDomain userDomain = UserDomain.builder()
                .username(updateReqDTO.getUsername())
                .email(updateReqDTO.getEmail())
                .profileImg(updateReqDTO.getProfileImg())
                .build();
        return ResponseEntity.ok(new UpdateResDTO().resultMessage(userUseCase.update(userDomain)));
    }

    @Override
    public ResponseEntity<UpdatePasswordResDTO> updatePassword(UpdatePasswordReqDTO updatePasswordReqDTO) throws Exception {
        UserDomain userDomain = UserDomain.builder()
                .newPassword(updatePasswordReqDTO.getNewPassword())
                .pastPassword(updatePasswordReqDTO.getPastPassword())
                .build();
        return ResponseEntity.ok(new UpdatePasswordResDTO().resultMessage(userUseCase.updatePassword(userDomain)));
    }
}
