package com.sw.cmc.adapter.in.user.web;

import com.sw.cmc.adapter.in.user.dto.CheckJoinResDTO;
import com.sw.cmc.adapter.in.user.dto.JoinReqDTO;
import com.sw.cmc.adapter.in.user.dto.JoinResDTO;
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
        return ResponseEntity.ok(joinUseCase.join(modelMapper.map(joinReqDTO, UserDomain.class)));
    }

    @Override
    public ResponseEntity<CheckJoinResDTO> checkUserId(String userId) throws Exception {
        return ResponseEntity.ok(joinUseCase.checkUserId(userId));
    }

    @Override
    public ResponseEntity<CheckJoinResDTO> checkUsername(String username) throws Exception {
        return ResponseEntity.ok(joinUseCase.checkUsername(username));
    }
}
