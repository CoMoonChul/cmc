package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : LiveChat
 * author         : ihw
 * date           : 2025. 2. 17.
 * description    : livechat entity
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class LiveChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT COMMENT '채팅 메시지 ID'")
    private Long liveChatId;

    @Column(columnDefinition = "BIGINT COMMENT '라이브 코딩 방 ID'")
    private Long liveCodingId;

    @Column(columnDefinition = "BIGINT COMMENT '메시지 보낸 사용자 ID'")
    private Long userNum;

    @Column(columnDefinition = "TEXT COMMENT '메시지 내용'")
    private String content;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP COMMENT '메시지 보낸 시간'")
    private LocalDateTime createdAt;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP COMMENT '메시지 수정 시간'")
    private LocalDateTime updatedAt;
}
