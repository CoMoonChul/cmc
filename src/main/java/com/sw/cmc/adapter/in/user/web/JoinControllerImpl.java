package com.sw.cmc.adapter.in.user.web;

import com.sw.cmc.adapter.in.user.dto.*;
import com.sw.cmc.application.port.in.user.JoinUseCase;
import com.sw.cmc.domain.user.UserDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.user
 * fileName       : JoinControllerImpl
 * author         : SungSuHan
 * date           : 2025-02-11
 * description    : join-controller
 */
@RestController
@RequiredArgsConstructor
public class JoinControllerImpl implements JoinControllerApi {

    private final JoinUseCase joinUseCase;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<JoinResDTO> join(JoinReqDTO joinReqDTO) throws Exception {

        UserDomain userDomain = UserDomain.builder()
                .userId(joinReqDTO.getUserId())
                .password(joinReqDTO.getPassword())
                .username(joinReqDTO.getUsername())
                .email(joinReqDTO.getEmail())
                .profileImg(joinReqDTO.getProfileImg())
                .build();

        return ResponseEntity.ok(modelMapper.map(joinUseCase.join(userDomain), JoinResDTO.class));
    }

    @Override
    public ResponseEntity<JoinGoogleResDTO> joinGoogle(JoinGoogleReqDTO joinGoogleReqDTO) throws Exception {
        UserDomain userDomain = UserDomain.builder()
                .userId(joinGoogleReqDTO.getUserId())
                .username(joinGoogleReqDTO.getUsername())
                .email(joinGoogleReqDTO.getEmail())
                .profileImg(joinGoogleReqDTO.getProfileImg())
                .build();
        return ResponseEntity.ok(modelMapper.map(joinUseCase.joinGoogle(userDomain), JoinGoogleResDTO.class));
    }

    @Override
    public ResponseEntity<CheckJoinResDTO> checkUserId(String userId) throws Exception {
        return ResponseEntity.ok(new CheckJoinResDTO().resultMessage(joinUseCase.checkUserId(userId)));
    }

    @Override
    public ResponseEntity<CheckJoinResDTO> checkUsername(String username) throws Exception {
        return ResponseEntity.ok(new CheckJoinResDTO().resultMessage(joinUseCase.checkUsername(username)));
    }
}
