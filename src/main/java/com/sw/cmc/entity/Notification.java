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

    @Column(nullable = false)
    private String userNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noti_template_id", referencedColumnName = "notiTemplateId", nullable = false)
    private NotificationTemplate notiTemplate;

    private String sendAt;

    private String sendState;

    private String linkUrl;

    private String createdAt;

    private Long createUser;


}
