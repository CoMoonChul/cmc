package com.sw.cmc.domain.lcd;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * packageName    : com.sw.cmc.domain.live_coding.model
 * fileName       : LiveCoding
 * author         : Ko
 * date           : 2025-02-08
 * description    : 라이브 코딩 도메인 객체
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveCodingDomain  {
    private UUID roomId;  // 방 ID (UUID)
    private Long hostId;  // 방장 ID (userNum)
    private LocalDateTime createdAt;  // 생성 일시
    private Integer participantCount;  // 참가자 수
    private List<Long> participants;  // 참가자 ID 목록 (userNum 리스트)




}
