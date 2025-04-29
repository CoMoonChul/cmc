package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : AIComment
 * author         : ihw
 * date           : 2025. 4. 29.
 * description    : ai comment entity
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ai_comment")
public class AIComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long targetId;

    @Column(name = "code_content", length = 20000, nullable = false)
    private String codeContent;

    @Column(name = "code_type", length = 20, nullable = false)
    private String codeType;

    @Column(name = "created_at", updatable = false, insertable = false)
    private String createdAt;

    @Column(name = "updated_at", insertable = false)
    private String updatedAt;
}
