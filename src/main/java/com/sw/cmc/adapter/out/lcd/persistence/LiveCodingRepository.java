package com.sw.cmc.adapter.out.lcd.persistence;

import com.sw.cmc.domain.lcd.LiveCodingDomain;

import java.util.UUID;

/**
 * packageName    : com.sw.cmc.adapter.out.lcd.persistence
 * fileName       : LiveCodingRepository
 * author         : 82104
 * date           : 2025-02-28
 * description    : LiveCoding Repository
 */
public interface LiveCodingRepository {

    /**
     * methodName : saveLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩방 저장 (레디스)
     *
     * @param liveCodingDomain - liveCodingDomain
     */
    void saveLiveCoding(LiveCodingDomain liveCodingDomain);

    /**
     * methodName : deleteLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩방 삭제 (레디스)
     *
     * @param roomId - 방 id
     * @return boolean
     */
    boolean deleteLiveCoding(UUID roomId);

    /**
     * methodName : findByRoomId
     * author : KO YOUNG SUNG
     * description : 라브코딩방 조회 (room Id)
     *
     * @param roomId - 방 id
     * @return live coding domain
     */
    LiveCodingDomain findByRoomId(UUID roomId);

    /**
     * methodName : findRoomIdByHostId
     * author : KO YOUNG SUNG
     * description : hostId로 roomId 찾기
     * @param hostId - 호스트 id
     * @return roomId
     */
    UUID findRoomIdByHostId(Long hostId);

    /**
     * methodName : existsByHostId
     * author : KO YOUNG SUNG
     * description : hostId로 방이 존재하는지 확인
     * @param hostId - 호스트 id
     * @return boolean
     */
    boolean existsByHostId(Long hostId);

}
