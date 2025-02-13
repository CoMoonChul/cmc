package com.sw.cmc.domain.lcd;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

/**
 * packageName    : com.sw.cmc.domain.live_coding.model
 * fileName       : LiveCoding
 * author         : Ko
 * date           : 2025-02-08
 * description    :
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

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 생성 일시


    @Column(nullable = false)
    private Long participantCount;  // 참가자 수

    @Builder
    public LiveCodingDomain(Long hostId) {
        this.hostId = hostId;
        this.createdAt = LocalDateTime.now();
        this.participantCount = 0L;
    }




}
