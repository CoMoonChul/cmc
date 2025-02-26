package com.sw.cmc.application.port.in.redis;

import java.util.Map;

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
     * @param key - 레디스 키
     * @param value - 레디스 값
     */
    void save(String key, String value) throws Exception;


    /**
     * methodName : select
     * author : KO YOUNG SUNG
     * description : 레디스 해쉬 값 조회
     *
     * @param key - 레디스 키
     * @return string
     */
    String select(String key) throws Exception;


    /**
     * methodName : delete
     * author : KO YOUNG SUNG
     * description : 레디스 값 삭제
     *
     * @param key - 레디스 키
     */
    void delete(String key) throws Exception;

    /**
     * methodName : saveHash
     * author : KO YOUNG SUNG
     * description : 레디스 해쉬 값 저장
     *
     * @param key - 레디스 키
     * @param hash - hash
     */
    void saveHash(String key, Map<String, String> hash) throws Exception;

    /**
     * methodName : selectHash
     * author : KO YOUNG SUNG
     * description : 레디스 해쉬 값 조회
     * @param key - 레디스 키
     * @return the hash
     */
    Map<String, String> selectHash(String key) throws Exception;


}
