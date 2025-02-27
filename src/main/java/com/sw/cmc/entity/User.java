package com.sw.cmc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : User
 * author         : ihw
 * date           : 2025. 2. 17.
 * description    : user entity
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT COMMENT '회원 번호'")
    private Long userNum;

    @NotNull
    @Column(columnDefinition = "VARCHAR(50) COMMENT '회원 ID'", unique = true)
    private String userId;

    @NotNull
    @Column(columnDefinition = "VARCHAR(50) COMMENT '회원 비밀번호'")
    private String password;

    @Column(columnDefinition = "VARCHAR(2048) COMMENT 'Refresh Token'")
    private String refreshToken;

    @NotNull
    @Column(columnDefinition = "VARCHAR(50) COMMENT '회원명'", unique = true)
    private String username;

    @NotNull
    @Column(columnDefinition = "VARCHAR(50) COMMENT '회원 이메일'")
    private String email;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '회원 권한'")
    private String userRole;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP COMMENT '회원 생성 일시'")
    private LocalDateTime createdAt;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP COMMENT '회원 변경 일시'")
    private LocalDateTime updatedAt;
}