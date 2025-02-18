package com.sw.cmc.application.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


/**
 * packageName    : com.sw.cmc.application.service.redis
 * fileName       : RedisService
 * author         : 82104
 * date           : 2025-02-18
 * description    :
 */
@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // 생성자 주입
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
