package com.sw.cmc.application.service.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * packageName    : com.sw.cmc.application.service.redis
 * fileName       : RedisServiceTest
 * author         : 82104
 * date           : 2025-02-18
 * description    :
 */
@SpringBootTest
class RedisServiceTest {
    @Autowired
    private RedisService redisService;

    @Test
    public void testRedisConnection() {
        // RedisService가 제대로 주입되었는지 확인
        assertNotNull(redisService, "RedisService should not be null.");
    }

    @Test
    public void testSetAndGetValue() {
        String key = "testKey";
        String value = "testValue";

        // Redis에 값 저장
        redisService.setValue(key, value);

        // Redis에서 값 가져오기
        String retrievedValue = redisService.getValue(key);

        // 값이 정상적으로 저장되고 가져왔는지 확인
        assertNotNull(retrievedValue, "The retrieved value should not be null.");
        assertEquals(value, retrievedValue, "The value retrieved from Redis should match the original value.");
    }}