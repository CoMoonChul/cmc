package com.sw.cmc.application.service.redis;

import com.sw.cmc.adapter.out.redis.persistence.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;


/**
 * packageName    : com.sw.cmc.application.service.redis
 * fileName       : RedisService
 * author         : 82104
 * date           : 2025-02-18
 * description    : redis Service
 */
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisRepository redisRepository;

    public void setValue(String key, String value) {
        redisRepository.save(key, value);
    }

    public String getValue(String key) {
        return redisRepository.get(key);
    }

    public void deleteKey(String key) {
        redisRepository.delete(key);
    }

    public Set<String> getAllKeys() {
        return redisRepository.getAllKeys();
    }
}
