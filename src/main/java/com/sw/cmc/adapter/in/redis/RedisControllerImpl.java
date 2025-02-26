package com.sw.cmc.adapter.in.redis;


import com.sw.cmc.adapter.in.redis.dto.*;
import com.sw.cmc.adapter.in.redis.web.RedisControllerApi;
import com.sw.cmc.application.port.in.redis.RedisUseCase;
import com.sw.cmc.domain.redis.RedisDomain;
import com.sw.cmc.domain.redis.RedisHashDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * packageName    : com.sw.cmc.adapter.in.redis
 * fileName       : RedisController
 * author         : 82104
 * date           : 2025-02-19
 * description    : redis controller
 */
@RestController
@RequiredArgsConstructor
public class RedisControllerImpl implements RedisControllerApi {
    private final RedisUseCase redisUseCase;

    @Override
    public ResponseEntity<SaveRedisResDTO> saveRedis(SaveRedisReqDTO saveRedisReqDTO) throws Exception {
        // RedisDomain 객체 생성
        RedisDomain redisDomain = RedisDomain.builder()
                .key(saveRedisReqDTO.getKey())
                .value(saveRedisReqDTO.getValue())
                .build();

        // Redis에 저장
        redisUseCase.save(redisDomain.getKey(), redisDomain.getValue());

        // 성공 응답 반환
        SaveRedisResDTO response = new SaveRedisResDTO();
        response.setMessage("성공적으로 저장됨");
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteRedis(DeleteRedisReqDTO deleteRedisReqDTO) throws Exception {
        // Redis에서 항목 삭제
        redisUseCase.delete(deleteRedisReqDTO.getKey());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<SelectRedisResDTO> selectRedis(String key) throws Exception {
        // 특정 키로 Redis 항목 조회
        String value = redisUseCase.select(key);
        if (value != null) {
            SelectRedisResDTO response = new SelectRedisResDTO();
            response.setKey(key);
            response.setValue(value);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<SaveHashResDTO> saveHash(@RequestBody SaveHashReqDTO saveHashReqDTO) throws Exception {
        RedisHashDomain redisHashDomain = RedisHashDomain.builder()
                .key(saveHashReqDTO.getKey())
                .hash(saveHashReqDTO.getHash())
                .build();
        redisUseCase.saveHash(redisHashDomain.getKey(), redisHashDomain.getHash());

        SaveHashResDTO response = new SaveHashResDTO();
        response.setMessage("해시가 성공적으로 저장되었습니다.");
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<SelectHashResDTO> selectHash(String key) throws Exception {
        Map<String, String> hash = redisUseCase.selectHash(key); // 변경된 부분
        if (hash != null && !hash.isEmpty()) {
            SelectHashResDTO response = new SelectHashResDTO();
            response.setHash(hash);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
