package com.sw.cmc.application.port.in.redis;

import com.sw.cmc.adapter.in.redis.dto.SelectRedisResDTO;
import com.sw.cmc.domain.redis.RedisDomain;

import java.util.List;

/**
 * packageName    : com.sw.cmc.application.port.in.redis
 * fileName       : RedisUseCase
 * author         : 82104
 * date           : 2025-02-19
 * description    :
 */
public interface RedisUseCase {

    void save(String key, String value);
    String get(String key);
    List<SelectRedisResDTO> getAll();
    void delete(String key);
}
