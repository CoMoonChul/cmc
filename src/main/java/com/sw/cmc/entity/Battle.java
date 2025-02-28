package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String title;

    private String content;

    private String endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_1", referencedColumnName = "codeEditNum", nullable = false)
    private Editor editorOne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_2", referencedColumnName = "codeEditNum", nullable = false)
    private Editor editorTwo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_num", referencedColumnName = "userNum", nullable = false)
    private User user;

    @Column(name = "created_at", updatable = false, insertable = false)
    private String createdAt;

    @Column(name = "updated_at", insertable = false)
    private String updatedAt;
}
