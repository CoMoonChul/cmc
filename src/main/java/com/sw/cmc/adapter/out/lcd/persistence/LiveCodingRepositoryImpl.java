package com.sw.cmc.adapter.out.lcd.persistence;

import com.sw.cmc.application.service.redis.RedisService;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


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
        liveCodingMap.put("createdAt", liveCodingDomain.getCreatedAt());
        liveCodingMap.put("participantCount", liveCodingDomain.getParticipantCount().toString());
        liveCodingMap.put("participants", String.join(",", liveCodingDomain.getParticipants().stream().map(String::valueOf).toArray(String[]::new)));

        // Redis에 방 정보 저장
        redisService.saveHash(REDIS_LIVE_CODING_PREFIX + liveCodingDomain.getRoomId().toString(), liveCodingMap);
    }

}