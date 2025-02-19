package com.sw.cmc.application.port.in.redis;

/**
 * packageName    : com.sw.cmc.application.port.in.redis
 * fileName       : RedisUseCase
 * author         : 82104
 * date           : 2025-02-19
 * description    : 레디스 유스케이스
 */
public interface RedisUseCase {

    /**
     * methodName : save
     * author : KO YOUNG SUNG
     * description : 레디스 값 저장
     *
     * @param key
     * @param value
     */
    void save(String key, String value);

    /**
     * methodName : get
     * author : KO YOUNG SUNG
     * description : 레디스 값 조회
     *
     * @param key
     * @return string
     */
    String get(String key);

    /**
     * methodName : delete
     * author : KO YOUNG SUNG
     * description : 레디스 값 삭제
     *
     * @param key
     */
    void delete(String key);
}
