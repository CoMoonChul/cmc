package com.sw.cmc.domain.user;

import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.sw.cmc.domain
 * fileName       : TempLoginDomain
 * author         : SungSuHan
 * date           : 2025-02-10
 * description    :
 */
@Getter
@Setter
public class TempLoginDomain {
    private long userNum;
    private String userId;
    private String password;
    private String username;
    private String email;
    private String userRole;
}
