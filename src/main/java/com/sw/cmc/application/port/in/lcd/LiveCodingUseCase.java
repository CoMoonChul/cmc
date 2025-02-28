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
    LiveCodingDomain createLiveCoding(Long hostId);

    /**
     * methodName : deleteLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩방 삭제
     *
     * @param roomId - 방 id
     * @return delete lcd case
     */
    DeleteLcdCase deleteLiveCoding(UUID roomId);  // 방 삭제 (상태 메시지 반환)


//    // 라이브 코딩 방 정보 업데이트
//    LiveCodingDomain updateLiveCoding(Long roomId, Long participantCount) throws Exception;
//
//    // 라이브 코딩 방 조회
//    LiveCodingDomain selectLiveCoding(Long roomId) throws Exception;
//
//    // 라이브 코딩 방 참가
//    LiveCodingDomain joinLiveCoding(Long roomId, Long participantId) throws Exception;
//
//    // 라이브 코딩 방 나가기
//    LiveCodingDomain leaveLiveCoding(Long roomId, Long userId) throws Exception;

}

