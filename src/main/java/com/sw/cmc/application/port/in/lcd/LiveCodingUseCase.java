package com.sw.cmc.application.port.in.lcd;

import com.sw.cmc.domain.lcd.LiveCodingDomain;

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
     * description : 라이브 코딩 방 생성
     *
     * @param hostId
     * @return live coding domain
     * @throws Exception the exception
     *
     */
    LiveCodingDomain createLiveCoding(Long hostId)  throws Exception;



    // 참가자 수 업데이트
//    LiveCodingDomain updateParticipantCount(Long roomId, Long newCount);

    // 방 삭제
    // void deleteLiveCoding(Long roomId);

}
