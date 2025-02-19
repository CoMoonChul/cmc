package com.sw.cmc.application.service.redis;

import com.sw.cmc.application.port.in.redis.RedisUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;


/**
 * packageName    : com.sw.cmc.application.service.redis
 * fileName       : RedisService
 * author         : 82104
 * date           : 2025-02-18
 * description    : redis Service
 */
@Service
@RequiredArgsConstructor
public class RedisService implements RedisUseCase {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void save(String key, String value) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, value);  // Redis에 키-값 쌍 저장
    }

    @Override
    public String get(String key) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get(key);  // Redis에서 키에 해당하는 값 조회
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);  // Redis에서 키 삭제
    }
}
