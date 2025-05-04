package com.sw.cmc.application.service.user;

import com.sw.cmc.adapter.out.user.persistence.UserRepository;
import com.sw.cmc.application.port.in.user.UserUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.user.UserDomain;
import com.sw.cmc.entity.User;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sw.cmc.common.constant.Constants.PROFILE_IMG_BASE_URL;
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
                .profileImg(user.getProfileImg())
                .build();
    }

    @Override
    @Transactional
    public String withdraw(UserDomain userDomain) throws Exception {
        // 회원 조회
        final User user = userRepository.findByUserNum(userUtil.getAuthenticatedUserNum())
                .orElseThrow(() -> new CmcException("USER001"));

        // 비밀번호 검사
        boolean isMatch = passwordEncoder.matches(userDomain.getPassword(), user.getPassword());
        if (!isMatch) {
            throw new CmcException("USER029");
        }

        // 회원 삭제
        userRepository.deleteByUserNum(userUtil.getAuthenticatedUserNum());

        return messageUtil.getFormattedMessage("USER014");
    }

    @Override
    @Transactional
    public String update(UserDomain userDomain) throws Exception {
        // 유효성 검사
        validateUsername(userDomain.getUsername());
        validateEmail(userDomain.getEmail());
        validateProfileImg(userDomain.getProfileImg());

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

        user.setUsername(userDomain.getUsername());
        user.setEmail(userDomain.getEmail());

        // 이미지 경로 미지정시 디폴트 이미지 세팅 처리
        if (StringUtils.isBlank(userDomain.getProfileImg())) {
            user.setProfileImg(PROFILE_IMG_BASE_URL + "default_profile.png");

        } else {
            user.setProfileImg(userDomain.getProfileImg());
        }

        // 회원 정보 저장
        userRepository.save(user);

        return messageUtil.getFormattedMessage("USER018");
    }

    @Override
    @Transactional
    public String updatePassword(UserDomain userDomain) throws Exception {
        // 유효성 검사
        validatePassword(userDomain.getNewPassword());
        validatePasswordChanged(userDomain.getNewPassword(), userDomain.getPastPassword());

        // 회원 조회
        final User user = userRepository.findByUserNum(userUtil.getAuthenticatedUserNum())
                .orElseThrow(() -> new CmcException("USER001"));

        // 기존 비밀번호가 잘못된 경우
        if (!passwordEncoder.matches(userDomain.getPastPassword(), user.getPassword())) {
            throw new CmcException("USER030");
        }

        user.setPassword(passwordEncoder.encode(userDomain.getNewPassword()));

        // 회원 정보 저장
        userRepository.save(user);

        return messageUtil.getFormattedMessage("USER032");
    }
}
