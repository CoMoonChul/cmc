package com.sw.cmc.domain.lcd;

import lombok.*;

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
    private String createdAt;  // 생성 일시
    private Integer participantCount;  // 참가자 수
    private List<Long> participants;;  // 참가자 ID 목록 (userNum 리스트)

    public void addParticipant(Long participantId) {
        if (participants == null) {
            // 추후 에러 처리
        }
        if (participants != null && !participants.contains(participantId)) {
            participants.add(participantId);
            participantCount++;
        }
    }
    // 참가자를 제거하는 메서드
    public void removeParticipant(Long participantId) {
        if (participants != null && participants.contains(participantId)) {
            participants.remove(participantId);
            participantCount--;
        }
    }

    // 방이 비어있는지 확인하는 메서드
    public boolean isRoomEmpty() {
        return participants == null || participants.isEmpty();
    }

    // 방 정보의 포맷을 반환하는 메서드 (예: 참가자 수 및 생성 일시)
    public String getRoomInfo() {
        return String.format("Room ID: %s | Host ID: %d | Created At: %s | Participants: %d",
                roomId.toString(), hostId, createdAt, participantCount);
    }



}
