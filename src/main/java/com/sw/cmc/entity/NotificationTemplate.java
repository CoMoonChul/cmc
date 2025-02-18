package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : NotificationTemplate
 * author         : dkstm
 * date           : 2025-02-18
 * description    :
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notiTemplateId;

    private String notiTemplateNm;

    private String notiTitle;

    private String notiContent;

    private String notiType;

    private String createdAt;

    private String createUser;
}
