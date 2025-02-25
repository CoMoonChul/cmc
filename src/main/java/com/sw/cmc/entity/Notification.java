package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : Notification
 * author         : ihw
 * date           : 2025. 2. 17.
 * description    : notification entity
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "created_at", updatable = false, insertable = false)
    private String createdAt;

    private Long createUser;

    private String reasonNoti;


}
