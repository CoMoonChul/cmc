package com.sw.cmc.adapter.in.lcd.web;

import com.sw.cmc.adapter.in.livecoding.dto.CreateLiveCodingReqDTO;
import com.sw.cmc.adapter.in.livecoding.dto.CreateLiveCodingResDTO;
import com.sw.cmc.adapter.in.livecoding.web.LiveCodingControllerApi;
import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.web
 * fileName       : LiveCodingController
 * author         : Ko
 * date           : 2025-02-08
 * description    : 실시간 코딩 controller
 */
@RestController
@RequiredArgsConstructor
public class LiveCodingControllerImpl implements LiveCodingControllerApi {
    private final LiveCodingUseCase liveCodingUseCase;

    @Override
    public ResponseEntity<CreateLiveCodingResDTO> createLiveCoding(CreateLiveCodingReqDTO createLiveCodingReqDTO) throws Exception {
        LiveCodingDomain liveCodingDomain = liveCodingUseCase.createLiveCoding(createLiveCodingReqDTO.getHostId());
        CreateLiveCodingResDTO response = new CreateLiveCodingResDTO();
        response.setRoomId(liveCodingDomain.getRoomId());
        return ResponseEntity.ok(response);  // 200 OK 응답 반환
    }

}



