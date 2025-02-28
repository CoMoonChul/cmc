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


    // 라이브 코딩 방 생성
    LiveCodingDomain createLiveCoding(Long hostId);




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

