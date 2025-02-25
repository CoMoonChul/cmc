package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : Editor
 * author         : An Seung Gi
 * date           : 2025-02-25
 * description    : CodeEditor entity
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "code_editor")
public class Editor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeEditNum;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private String language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_num", referencedColumnName = "userNum", nullable = false)
    private User user;

    @Column(name = "created_at", updatable = false, insertable = false)
    private String createdAt;

    @Column(name = "updated_at", insertable = false)
    private String updatedAt;
}
