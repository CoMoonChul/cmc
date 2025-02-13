package com.sw.cmc.domain.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * packageName    : com.sw.cmc.domain
 * fileName       : TempLoginDomain
 * author         : SungSuHan
 * date           : 2025-02-10
 * description    :
 */
@Getter
@Setter
public class TempLogin {
    private long userNum;
    private String userId;
    private String password;
    private String userName;
    private String email;
    private String userRole;
    private String createdAt;
    private String updatedAt;
}
