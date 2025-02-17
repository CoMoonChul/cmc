package com.sw.cmc.application.service.lcd;

import com.sw.cmc.adapter.out.lcd.persistence.LiveCodingRepository;
import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.common.util.LoginUtil;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.sw.cmc.application.service
 * fileName       : LiveCodingService
 * author         : Ko
 * date           : 2025-02-08
 * description    : LiveCoding service
 */
@Service
@RequiredArgsConstructor
public class LiveCodingService implements LiveCodingUseCase {

    private final LoginUtil loginUtil;
    private final MessageUtil messageUtil;
    private final ModelMapper modelMapper;
    private final LiveCodingRepository liveCodingRepository;

    // 방 생성
    @Override
    public LiveCodingDomain createLiveCoding(Long hostId) {
        return null;
    }

//    // 예시: 참가자 수 증가
//    @Override
//    public LiveCodingDomain increaseParticipant(Long roomId) {
//        // 추후 구현
//        return null;
//    }





}
