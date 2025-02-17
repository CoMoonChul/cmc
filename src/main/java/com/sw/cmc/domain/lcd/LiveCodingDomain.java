package com.sw.cmc.domain.lcd;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * packageName    : com.sw.cmc.domain.live_coding.model
 * fileName       : LiveCoding
 * author         : Ko
 * date           : 2025-02-08
 * description    : 라이브 코딩 도메인 객체
 */
@Getter
@Entity
@NoArgsConstructor
public class LiveCodingDomain  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long roomId;  // 방 ID (Primary Key)

    @Column(nullable = false)
    private Long hostId;  // 방장 ID

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 생성 일시

    @Column(nullable = false)
    private Long participantCount;  // 참가자 수

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(); // 엔터티가 저장되기 전에 현재 시간을 설정
        }
    }

    @Builder
    public LiveCodingDomain(Long hostId) {
        this.hostId = hostId;
        this.createdAt = LocalDateTime.now();
        this.participantCount = 0L;
    }




}
