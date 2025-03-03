package com.sw.cmc.application.service.user;

import com.sw.cmc.adapter.out.user.persistence.UserRepository;
import com.sw.cmc.application.port.in.user.UserUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.user.UserDomain;
import com.sw.cmc.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sw.cmc.domain.user.UserDomain.*;

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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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
    @Transactional
    public String withdraw(UserDomain userDomain) throws Exception {
        // 회원 조회
        final User user = userRepository.findByUserNum(userUtil.getAuthenticatedUserNum())
                .orElseThrow(() -> new CmcException("USER001"));

        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserId(), userDomain.getPassword())
        );

        // 회원 삭제
        userRepository.deleteByUserNum(userUtil.getAuthenticatedUserNum());

        return messageUtil.getFormattedMessage("USER014");
    }

    @Override
    @Transactional
    public String update(UserDomain userDomain) throws Exception {
        // 유효성 검사
        validatePassword(userDomain.getPassword());
        validateUsername(userDomain.getUsername());
        validateEmail(userDomain.getEmail());

        // 닉네임 중복 검사
        Optional<User> usernameOptional = userRepository.findByUsername(userDomain.getUsername());
        if (usernameOptional.isPresent() && !usernameOptional.get().getUserNum().equals(userUtil.getAuthenticatedUserNum())) {
            throw new CmcException("USER009");
        }
        // 이메일 중복 검사
        Optional<User> emailOptional = userRepository.findByEmail(userDomain.getEmail());
        if (emailOptional.isPresent() && !emailOptional.get().getUserNum().equals(userUtil.getAuthenticatedUserNum())) {
            throw new CmcException("USER019");
        }

        // 회원 조회
        final User user = userRepository.findByUserNum(userUtil.getAuthenticatedUserNum())
                .orElseThrow(() -> new CmcException("USER001"));

        user.setPassword(passwordEncoder.encode(userDomain.getPassword()));
        user.setUsername(userDomain.getUsername());
        user.setEmail(userDomain.getEmail());

        // 회원 정보 저장
        userRepository.save(user);

        return messageUtil.getFormattedMessage("USER018");
    }
}
