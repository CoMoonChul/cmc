package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : Vote
 * author         : ihw
 * date           : 2025. 3. 1.
 * description    : vote entity
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @Column(nullable = false)
    private Long battleId;

    @Column(nullable = false)
    private Integer voteValue;

    @Column(name = "created_at", updatable = false, insertable = false)
    private String createdAt;

    @Column(name = "updated_at", insertable = false)
    private String updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_num", referencedColumnName = "userNum", nullable = false)
    private User user;
}
