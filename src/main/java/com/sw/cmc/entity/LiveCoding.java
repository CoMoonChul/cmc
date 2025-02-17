package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : LiveCoding
 * author         : ihw
 * date           : 2025. 2. 17.
 * description    : live coding entity
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class LiveCoding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT COMMENT '라이브 코딩 방 ID'")
    private Long liveCodingId;

    @Column(columnDefinition = "BIGINT COMMENT '방장 회원 ID'")
    private Long hostId;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP COMMENT '라이브 코딩 생성 일시'")
    private LocalDateTime createdAt;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP COMMENT '라이브 코딩 방 업데이트 일시'")
    private LocalDateTime updatedAt;
}
