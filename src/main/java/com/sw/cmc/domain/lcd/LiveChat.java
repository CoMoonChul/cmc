package com.sw.cmc.domain.lcd;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * packageName    : com.sw.cmc.domain.live_coding.model
 * fileName       : LiveChat
 * author         : Ko
 * date           : 2025-02-08
 * description    :
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LiveChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private LiveCoding liveCoding;


    private String message;  // 채팅 메시지
    private String sender;  // 메시지 보낸 사람

    private LocalDateTime timestamp;  // 메시지 보낸 시간

    // 생성 시점에 타임스탬프 자동 설정
    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

}
