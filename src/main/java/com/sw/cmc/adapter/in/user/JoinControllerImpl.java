package com.sw.cmc.adapter.in.user;

import com.sw.cmc.adapter.in.user.dto.JoinCheckResponse;
import com.sw.cmc.adapter.in.user.dto.JoinRequest;
import com.sw.cmc.adapter.in.user.dto.JoinResponse;
import com.sw.cmc.adapter.in.user.web.JoinControllerApi;
import com.sw.cmc.application.port.in.user.JoinUseCase;
import com.sw.cmc.domain.user.Join;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.user
 * fileName       : JoinControllerImpl
 * author         : SungSuHan
 * date           : 2025-02-11
 * description    :
 */
@RestController
@RequiredArgsConstructor
public class JoinControllerImpl implements JoinControllerApi {

    private final JoinUseCase joinUseCase;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<JoinResponse> join(JoinRequest joinRequest) throws Exception {
        return ResponseEntity.ok(joinUseCase.join(modelMapper.map(joinRequest, Join.class)));
    }

    @Override
    public ResponseEntity<JoinCheckResponse> checkUserId(String userId) throws Exception {
        return ResponseEntity.ok(modelMapper.map(joinUseCase.checkUserId(userId), JoinCheckResponse.class));
    }

    @Override
    public ResponseEntity<JoinCheckResponse> checkUserName(String userName) throws Exception {
        return ResponseEntity.ok(modelMapper.map(joinUseCase.checkUserName(userName), JoinCheckResponse.class));
    }
}
