package com.sw.cmc.application.service.lcd;

import com.sw.cmc.adapter.out.lcd.persistence.LiveCodingRepository;
import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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

    private final LiveCodingRepository liveCodingRepository;  // LiveCodingRepository 주입



    @Override
    public LiveCodingDomain createLiveCoding(Long hostId) {

        // 방 ID 생성 (UUID)
        UUID roomId = UUID.randomUUID();

        String createdAt = String.valueOf(LocalDateTime.now());

        // 초기 참가자 목록 (방장만 포함)
        List<Long> participants = new ArrayList<>();
        participants.add(hostId);

        // 참가자 수 초기화 (방장 포함)
        Integer participantCount = 1;

        // LiveCodingDomain 객체 생성
        LiveCodingDomain liveCodingDomain = new LiveCodingDomain(
                roomId,  // 생성된 방 ID
                hostId,  // 방장 ID
                createdAt,  // 방 생성 시간
                participantCount,  // 참가자 수
                participants  // 참가자 목록
        );

        // Redis에 방 정보 저장
        liveCodingRepository.saveLiveCoding(liveCodingDomain);  // Repository 사용

        return liveCodingDomain;  // 생성된 LiveCodingDomain 반환
    }
}
