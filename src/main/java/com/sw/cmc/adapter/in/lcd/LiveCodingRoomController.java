package com.sw.cmc.adapter.in.lcd;

import com.sw.cmc.application.service.live_code.LiveCodingRoomService;
import com.sw.cmc.domain.lcd.LiveCodingRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : com.sw.cmc.adapter.in.web
 * fileName       : LiveCodingRoomController
 * author         : Ko
 * date           : 2025-02-08
 * description    :
 */
@RestController
@RequestMapping("/api/livecoding")
@RequiredArgsConstructor
public class LiveCodingRoomController {

    private final LiveCodingRoomService liveCodingRoomService;

    // LiveCodingRoom 생성
    @PostMapping("/create")
    public LiveCodingRoom createRoom(@RequestParam int maxParticipants) {
        return liveCodingRoomService.createRoom(maxParticipants);
    }

    // 특정 LiveCodingRoom 조회
    @GetMapping("/{roomId}")
    public LiveCodingRoom getRoom(@PathVariable Long roomId) {
        return liveCodingRoomService.getRoom(roomId);
    }
}



