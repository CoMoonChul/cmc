package com.sw.cmc.adapter.in.lcd;

import com.sw.cmc.application.service.lcd.LiveCodingService;
import com.sw.cmc.domain.lcd.LiveCoding;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : com.sw.cmc.adapter.in.web
 * fileName       : LiveCodingController
 * author         : Ko
 * date           : 2025-02-08
 * description    :
 */
@RestController
@RequiredArgsConstructor
public class LiveCodingControllerImpl {

    private final LiveCodingService liveCodingService;

    // LiveCoding 생성
    @PostMapping("/create")
    public LiveCoding createRoom(@RequestParam int maxParticipants) {
        return liveCodingService.createRoom(maxParticipants);
    }

    // 특정 LiveCoding 조회
    @GetMapping("/{roomId}")
    public LiveCoding getRoom(@PathVariable Long roomId) {
        return liveCodingService.getRoom(roomId);
    }
}



