package com.sw.cmc.adapter.out.lcd.persistence;

import com.sw.cmc.domain.lcd.LiveCodingDomain;

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
     * description : 라이브 코딩방 생성
     *
     * @param liveCodingDomain - liveCodingDomain
     */
    void saveLiveCoding(LiveCodingDomain liveCodingDomain);
}
