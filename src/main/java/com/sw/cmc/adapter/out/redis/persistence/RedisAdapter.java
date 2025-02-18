package com.sw.cmc.adapter.out.redis.persistence;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.sw.cmc.adapter.out.redis.persistence
 * fileName       : RedisAdapter
 * author         : 82104
 * date           : 2025-02-18
 * description    : redis adapter
 */
@Component
public class RedisAdapter {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisAdapter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToRedis(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getFromRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
