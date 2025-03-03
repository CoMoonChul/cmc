package com.sw.cmc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : Group
 * author         : SungSuHan
 * date           : 2025-03-03
 * description    :
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "GroupTable")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT COMMENT '그룹 ID'")
    private Long groupId;

    @NotNull
    @Column(columnDefinition = "VARCHAR(50) COMMENT '그룹명'", unique = true)
    private String groupName;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP COMMENT '그룹 생성 일시'")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMember> groupMembers = new ArrayList<>();

    public void addGroupMember(GroupMember groupMember) {
        this.groupMembers.add(groupMember);
        groupMember.setGroup(this);
    }
}
