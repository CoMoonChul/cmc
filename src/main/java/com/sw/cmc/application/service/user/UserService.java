package com.sw.cmc.application.service.user;

import com.sw.cmc.adapter.out.user.persistence.UserRepository;
import com.sw.cmc.application.port.in.user.UserUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.user.UserDomain;
import com.sw.cmc.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.sw.cmc.application.service.user
 * fileName       : UserService
 * author         : SungSuHan
 * date           : 2025-02-27
 * description    :
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserUtil userUtil;
    private final MessageUtil messageUtil;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDomain getMyInfo() throws Exception {
        // 회원 조회
        final User user = userRepository.findByUserNum(userUtil.getAuthenticatedUserNum())
                .orElseThrow(() -> new CmcException("USER001"));

        return UserDomain.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    public String withdraw(UserDomain userDomain) throws Exception {
        return null;
    }
}
