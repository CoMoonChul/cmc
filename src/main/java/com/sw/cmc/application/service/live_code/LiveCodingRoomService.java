package com.sw.cmc.application.service.live_code;

import com.sw.cmc.adapter.out.persistence.repositroy.LiveCodingRoomRepository;
import com.sw.cmc.domain.lcd.LiveCodingRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.sw.cmc.application.service
 * fileName       : LiveCodingRoomService
 * author         : Ko
 * date           : 2025-02-08
 * description    :
 */
@Service
@RequiredArgsConstructor
public class LiveCodingRoomService {
    private final LiveCodingRoomRepository liveCodingRoomRepository;

    private static final int MAX_PARTICIPANTS = 99; // 최대인원


    // 새로운 LiveCodingRoom 생성
    public LiveCodingRoom createRoom(int maxParticipants) {
        // 최대인원
        if (maxParticipants == 0) {
            maxParticipants = MAX_PARTICIPANTS;
        }

        LiveCodingRoom liveCodingRoom = LiveCodingRoom.builder()
                .maxParticipants(maxParticipants)
                .build();
        return liveCodingRoomRepository.save(liveCodingRoom);
    }

    // 특정 LiveCodingRoom 조회
    public LiveCodingRoom getRoom(Long roomId) {
        return liveCodingRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("LiveCodingRoom not found"));
    }


}
