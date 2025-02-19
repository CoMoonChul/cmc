package com.sw.cmc.adapter.in.redis;


import com.sw.cmc.application.port.in.redis.RedisUseCase;
import com.sw.cmc.domain.redis.RedisDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * packageName    : com.sw.cmc.adapter.in.redis
 * fileName       : RedisController
 * author         : 82104
 * date           : 2025-02-19
 * description    : redis controller
 */
@RestController
@RequiredArgsConstructor
public class RedisController  {
    private final ModelMapper modelMapper;
    private final RedisUseCase redisUseCase;

    @Override
    public ResponseEntity<RedisResDTO> saveRedis(RedisReqDTO redisReqDTO) throws Exception {
        RedisDomain redisDomain = modelMapper.map(redisReqDTO, RedisDomain.class);
        redisUseCase.save(redisReqDTO.getKey(), redisDomain);
        return ResponseEntity.ok(modelMapper.map(redisDomain, RedisResDTO.class));
    }

    @Override
    public ResponseEntity<RedisResDTO> getRedis(String key) throws Exception {
        RedisDomain redisDomain = redisUseCase.get(key);
        return ResponseEntity.ok(modelMapper.map(redisDomain, RedisResDTO.class));
    }

    @Override
    public ResponseEntity<Void> deleteRedis(String key) throws Exception {
        redisUseCase.delete(key);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<RedisResDTO>> getAllRedis() throws Exception {
        List<RedisDomain> redisDomains = redisUseCase.getAll();
        List<RedisResDTO> response = redisDomains.stream()
                .map(domain -> modelMapper.map(domain, RedisResDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
