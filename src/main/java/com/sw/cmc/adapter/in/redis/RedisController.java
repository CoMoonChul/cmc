package com.sw.cmc.adapter.in.redis;

import com.sw.cmc.application.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Set;


/**
 * packageName    : com.sw.cmc.adapter.in.redis
 * fileName       : RedisController
 * author         : 82104
 * date           : 2025-02-19
 * description    : redis controller
 */
@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {
    private final RedisService redisService;

    // 키-값 저장
    @PostMapping("/save")
    public String saveKeyValue(@RequestParam String key, @RequestParam String value) {
        redisService.setValue(key, value);
        return "Key-Value pair saved successfully.";
    }

    // 키로 값 조회
    @GetMapping("/get")
    public String getValue(@RequestParam String key) {
        String value = redisService.getValue(key);
        return value != null ? value : "Key not found.";
    }

    // 키 삭제
    @DeleteMapping("/delete")
    public String deleteKey(@RequestParam String key) {
        redisService.deleteKey(key);
        return "Key deleted successfully.";
    }

    // 모든 키 조회
    @GetMapping("/keys")
    public Set<String> getAllKeys() {
        return redisService.getAllKeys();
    }

}
