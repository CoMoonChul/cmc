package com.sw.cmc.adapter.in.lcd.web;

import com.sw.cmc.adapter.in.livecode.dto.CreateLiveCodingReqDTO;
import com.sw.cmc.adapter.in.livecode.dto.CreateLiveCodingResDTO;
import com.sw.cmc.adapter.in.livecode.web.LiveCodingControllerApi;
import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final ModelMapper modelMapper;


    @Override
    public ResponseEntity<CreateLiveCodingResDTO> createLiveCoding(@RequestBody CreateLiveCodingReqDTO liveCodingRequest) throws Exception {
        // 요청 DTO를 도메인 객체로 변환
        LiveCodingDomain liveCodingDomain = liveCodingUseCase.createLiveCoding(liveCodingRequest.getHostId());
        // 도메인 객체를 응답 DTO로 변환
        CreateLiveCodingResDTO response = modelMapper.map(liveCodingDomain, CreateLiveCodingResDTO.class);

        return ResponseEntity.ok(response);
    }



}



