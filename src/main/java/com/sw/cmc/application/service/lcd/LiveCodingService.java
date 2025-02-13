package com.sw.cmc.application.service.lcd;

import com.sw.cmc.adapter.out.persistence.lcd.LiveCodingRepository;
import com.sw.cmc.domain.lcd.LiveCoding;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.sw.cmc.application.service
 * fileName       : LiveCodingService
 * author         : Ko
 * date           : 2025-02-08
 * description    :
 */
@Service
@RequiredArgsConstructor
public class LiveCodingService {
    private final LiveCodingRepository liveCodingRepository;

    private static final int MAX_PARTICIPANTS = 99; // 최대인원


    // 새로운 LiveCoding 생성
    public LiveCoding createRoom(int maxParticipants) {
        // 최대인원
        if (maxParticipants == 0) {
            maxParticipants = MAX_PARTICIPANTS;
        }

        LiveCoding liveCoding = LiveCoding.builder()
                .maxParticipants(maxParticipants)
                .build();
        return liveCodingRepository.save(liveCoding);
    }

    // 특정 LiveCoding 조회
    public LiveCoding getRoom(Long roomId) {
        return liveCodingRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("LiveCoding not found"));
    }


}
