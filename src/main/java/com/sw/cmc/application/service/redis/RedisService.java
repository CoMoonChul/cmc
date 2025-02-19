package com.sw.cmc.application.service.redis;

import com.sw.cmc.adapter.in.redis.dto.SelectRedisResDTO;
import com.sw.cmc.application.port.in.redis.RedisUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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
        ops.set(key, value);
    }

    @Override
    public String get(String key) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    @Override
    public List<SelectRedisResDTO> getAll() {
        return redisTemplate.keys("*").stream()
                .map(key -> {
                    String value = redisTemplate.opsForValue().get(key);
                    SelectRedisResDTO dto = new SelectRedisResDTO();
                    dto.setKey(key);
                    dto.setValue(value);
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
