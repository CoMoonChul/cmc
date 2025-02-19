package com.sw.cmc.domain.user;

import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.sw.cmc.domain.user
 * fileName       : LoginDomain
 * author         : SungSuHan
 * date           : 2025-02-17
 * description    :
 */
@Getter
@Setter
public class LoginDomain {
    private long userNum;
    private String userId;
    private String password;
    private String username;
    private String email;
    private String userRole;
}
