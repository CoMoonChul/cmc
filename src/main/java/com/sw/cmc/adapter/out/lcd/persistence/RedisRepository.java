package com.sw.cmc.adapter.out.lcd.persistence;

import com.sw.cmc.application.port.in.redis.RedisUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * packageName    : com.sw.cmc.application.service.redis
 * fileName       : RedisService
 * author         : 82104
 * date           : 2025-02-18
 * description    : redis Service
 */
@Service
@RequiredArgsConstructor
public class RedisRepository implements RedisUseCase {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void save(String key, String value) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, value);  // Redis에 키-값 쌍 저장
    }

    @Override
    public String select(String key) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get(key);  // Redis에서 키에 해당하는 값 조회
    }

    @Override
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));  // 성공시 true, 실패시 false
    }

    @Override
    public void saveHash(String key, Map<String, String> hash) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.putAll(key, hash);
    }

    @Override
    public Map<String, String> selectHash(String key) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        Map<String, String> result = hashOps.entries(key);
        return result.isEmpty() ? null : result;
    }

    @Override
    public void updateHashValue(String key, String field, String value) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.put(key, field, value);
    }

}
