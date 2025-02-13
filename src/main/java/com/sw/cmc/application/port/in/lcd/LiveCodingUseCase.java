package com.sw.cmc.application.port.in.lcd;

import com.sw.cmc.domain.lcd.LiveCodingDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.lcd
 * fileName       : LiveCodingUseCase
 * author         : Ko
 * date           : 2025-02-16
 * description    :
 */
public interface LiveCodingUseCase {

    // 방 생성
    LiveCodingDomain createLiveCoding(Long hostId);

    // 참가자 수 업데이트
//    LiveCodingDomain updateParticipantCount(Long roomId, Long newCount);

    // 방 삭제
    // void deleteLiveCoding(Long roomId);

}
