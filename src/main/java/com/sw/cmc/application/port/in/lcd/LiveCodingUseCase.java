package com.sw.cmc.application.port.in.lcd;

import com.sw.cmc.domain.lcd.LiveCodingDomain;

import java.util.UUID;

/**
 * packageName    : com.sw.cmc.application.port.in.lcd
 * fileName       : LiveCodingUseCase
 * author         : Ko
 * date           : 2025-02-16
 * description    : LiveCoding useCase
 */
public interface LiveCodingUseCase {


    /**
     * methodName : createLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩방 생성
     *
     * @param hostId - 호스트 id
     * @return live coding domain
     */
    // 라이브 코딩 방 생성
    LiveCodingDomain createLiveCoding(Long hostId) throws Exception;

    /**
     * methodName : deleteLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩방 삭제
     *
     * @param roomId - 방 id
     * @return boolean 성공 true 실패 false
     */
    boolean deleteLiveCoding(UUID roomId) throws Exception;  // 방 삭제 (상태 메시지 반환)

    /**
     * methodName : generateInviteLink
     * author : KO YOUNG SUNG
     * description : 라이브코딩방 초대링크 생성
     *
     * @param roomId - 방 id
     * @return String - 초대링크
     */
    String generateInviteLink(UUID roomId) throws Exception;


    /**
     * methodName : selectLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩 방 조회
     *
     * @param roomId - 방 id
     * @return live coding domain
     * @throws Exception the exception
     */
    LiveCodingDomain selectLiveCoding(UUID roomId) throws Exception;

    /**
     * methodName : updateLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩 방 업데이트
     *
     * @param roomId - 방 id
     * @param userNum - 유저 id
     * @param action - JOIN(0) / LEAVE(1)
     * @return live coding domain
     * @throws Exception the exception
     */
    LiveCodingDomain updateLiveCoding(UUID roomId, Long userNum, int action) throws Exception;



}

