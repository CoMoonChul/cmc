package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : Notification
 * author         : ihw
 * date           : 2025. 2. 17.
 * description    : notification entity
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notiId;

    private String userNum;

    private Long notiTemplateId;

    private String sendAt;

    private String createdAt;

    private Long createUser;
}
