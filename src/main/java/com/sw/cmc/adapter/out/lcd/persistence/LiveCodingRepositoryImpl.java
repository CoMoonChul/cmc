package com.sw.cmc.adapter.out.lcd.persistence;

import com.sw.cmc.application.service.redis.RedisService;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * packageName    : com.sw.cmc.adapter.out.persistence.repository
 * fileName       : LiveCodingRepository
 * author         : Ko
 * date           : 2025-02-08
 * description    : LiveCoding Repository Impl
 */
@Repository
@RequiredArgsConstructor  // final 필드에 대한 생성자 자동 생성
public class LiveCodingRepositoryImpl implements LiveCodingRepository {


    private final RedisService redisService;  // RedisService 주입
    private static final String REDIS_LIVE_CODING_PREFIX = "live_coding:";  // Redis에 저장할 키 접두사

    @Override
    public void saveLiveCoding(LiveCodingDomain liveCodingDomain) {
        // LiveCodingDomain 객체를 Redis에 저장할 Map으로 변환
        Map<String, String> liveCodingMap = new HashMap<>();
        liveCodingMap.put("roomId", liveCodingDomain.getRoomId().toString());
        liveCodingMap.put("hostId", liveCodingDomain.getHostId().toString());
        liveCodingMap.put("createdAt", String.valueOf(liveCodingDomain.getCreatedAt()));
        liveCodingMap.put("participantCount", liveCodingDomain.getParticipantCount().toString());
        liveCodingMap.put("participants", String.join(",", liveCodingDomain.getParticipants().stream().map(String::valueOf).toArray(String[]::new)));

        // Redis에 방 정보 저장
        redisService.saveHash(REDIS_LIVE_CODING_PREFIX + liveCodingDomain.getRoomId().toString(), liveCodingMap);
    }

    @Override
    public boolean deleteLiveCoding(UUID roomId) {
        // Redis에서 방을 삭제하는 로직
        String key = "live_coding:" + roomId;  // Redis에서 사용할 key
        return redisService.delete(key);  // Redis에서 해당 key 삭제
    }


    @Override
    public LiveCodingDomain findByRoomId(UUID roomId) {
        String key = REDIS_LIVE_CODING_PREFIX + roomId;
        Map<String, String> liveCodingMap = redisService.selectHash(key);  // 수정된 부분: Map<String, String>으로 처리

        if (liveCodingMap == null || liveCodingMap.isEmpty()) {
            return null;  // 방이 없으면 null 반환
        }

        UUID retrievedRoomId = UUID.fromString(liveCodingMap.get("roomId"));
        Long hostId = Long.valueOf(liveCodingMap.get("hostId"));

        String createdAtStr = liveCodingMap.get("createdAt");
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime createdAt = LocalDateTime.parse(createdAtStr, formatter);

        Integer participantCount = Integer.valueOf(liveCodingMap.get("participantCount"));
        List<Long> participants = Arrays.stream(liveCodingMap.get("participants").split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        return new LiveCodingDomain(retrievedRoomId, hostId, createdAt, participantCount, participants);
    }

}