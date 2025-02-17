package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : Battle
 * author         : ihw
 * date           : 2025. 2. 17.
 * description    : battle entity
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long battleId;

    private Long userNum;

    private String title;

    private String content;

    private Long code1;

    private Long code2;

    private String endTime;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;
}
