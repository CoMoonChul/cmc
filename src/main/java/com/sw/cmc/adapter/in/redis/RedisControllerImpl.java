package com.sw.cmc.adapter.in.redis;


import com.sw.cmc.adapter.in.redis.dto.DeleteRedisReqDTO;
import com.sw.cmc.adapter.in.redis.dto.SaveRedisReqDTO;
import com.sw.cmc.adapter.in.redis.dto.SaveRedisResDTO;
import com.sw.cmc.adapter.in.redis.dto.SelectRedisResDTO;
import com.sw.cmc.adapter.in.redis.web.RedisControllerApi;
import com.sw.cmc.application.port.in.redis.RedisUseCase;
import com.sw.cmc.domain.redis.RedisDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    private final ModelMapper modelMapper;
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
    public ResponseEntity<SelectRedisResDTO> getRedisByKey(String key) throws Exception {
        // 특정 키로 Redis 항목 조회
        String value = redisUseCase.get(key);
        if (value != null) {
            SelectRedisResDTO response = new SelectRedisResDTO();
            response.setKey(key);
            response.setValue(value);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
