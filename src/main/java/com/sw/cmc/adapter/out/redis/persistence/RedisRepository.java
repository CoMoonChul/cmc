package com.sw.cmc.adapter.out.redis.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * packageName    : com.sw.cmc.adapter.out.redis.persistence
 * fileName       : RedisAdapter
 * author         : 82104
 * date           : 2025-02-18
 * description    : redis repository
 */
@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public Set<String> getAllKeys() {
        System.out.println('f');
        System.out.println('f');
        System.out.println('f');
        System.out.println('f');
        return redisTemplate.keys("*");
    }
}
