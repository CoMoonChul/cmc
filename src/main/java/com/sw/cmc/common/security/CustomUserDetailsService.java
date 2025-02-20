package com.sw.cmc.common.security;

import com.sw.cmc.entity.User;
import com.sw.cmc.adapter.out.user.persistence.LoginRepository;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * packageName    : com.sw.cmc.domain.user
 * fileName       : CustomUserDetailsService
 * author         : SungSuHan
 * date           : 2025-02-17
 * description    : 사용자 인증 정보 서비스
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MessageUtil messageUtil;
    private final LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user =  loginRepository.findByUserId(userId)
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("USER001")));

        return new CustomUserDetails(user.getUserId(), user.getPassword(), user.getUserNum(), new ArrayList<>());
    }

}