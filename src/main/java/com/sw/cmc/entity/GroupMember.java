package com.sw.cmc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : GroupMember
 * author         : SungSuHan
 * date           : 2025-03-03
 * description    :
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT COMMENT '그룹 멤버 ID'")
    private Long groupMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId", referencedColumnName = "groupId", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userNum", referencedColumnName = "userNum", nullable = false)
    private User user;

    @NotNull
    @Column(columnDefinition = "VARCHAR(10) COMMENT '그룹 권한'")
    private String groupRole;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP COMMENT '그룹 생성 일시'")
    private LocalDateTime createdAt;
}
