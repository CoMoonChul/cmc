package com.sw.cmc.domain.lcd;

import com.sw.cmc.common.advice.CmcException;
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
    private String link;    // 링크

    // 최대 참가 인원
    private static final int MAX_PARTICIPANT_COUNT = 3;

    // 참가자 추가 메서드
    public void joinParticipant(Long userNum) {
        if (participants.contains(userNum)) {
            throw new CmcException("LCD005");
        }
        if (participants.size() >= MAX_PARTICIPANT_COUNT) {
            throw new CmcException("LCD007");
        }
        participants.add(userNum);
        this.participantCount = participants.size(); // 추가 후 업데이트
    }

    // 참가자 제거 메서드
    public void leaveParticipant(Long userNum) {
        if (!participants.contains(userNum)) {
            throw new CmcException("LCD006");
        }
        participants.remove(userNum);
        this.participantCount = participants.size();
    }


}
