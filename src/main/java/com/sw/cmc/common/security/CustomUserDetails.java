package com.sw.cmc.common.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * packageName    : com.sw.cmc.common.security
 * fileName       : CustomUserDetails
 * author         : SungSuHan
 * date           : 2025-02-20
 * description    : 사용자 인증 정보
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final String userId;
    private final String password;
    private final Long userNum;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String userId, String password, Long userNum, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.password = password;
        this.userNum = userNum;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

}
