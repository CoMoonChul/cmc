package com.sw.cmc.adapter.out.redis.persistence;

import com.sw.cmc.domain.redis.RedisDomain;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.sw.cmc.adapter.out.redis.persistence
 * fileName       : RedisAdapter
 * author         : 82104
 * date           : 2025-02-18
 * description    : redis repository
 */
@Repository
public class RedisRepository  {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Redis에 데이터 저장
    public void save(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Redis에서 데이터 조회
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Redis에서 데이터 삭제
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
