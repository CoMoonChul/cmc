package com.sw.cmc.application.service.user;

import com.sw.cmc.adapter.out.user.persistence.JoinRepository;
import com.sw.cmc.adapter.out.user.persistence.UserRepository;
import com.sw.cmc.application.port.in.user.JoinUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.common.util.NotiUtil;
import com.sw.cmc.common.util.SmtpUtil;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.user.UserDomain;
import com.sw.cmc.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

import static com.sw.cmc.domain.user.UserDomain.*;
import static com.sw.cmc.common.constant.Constants.PROFILE_IMG_BASE_URL;

/**
 * packageName    : com.sw.cmc.application.service.user
 * fileName       : JoinService
 * author         : SungSuHan
 * date           : 2025-02-11
 * description    :
 */
@Service
@RequiredArgsConstructor
public class JoinService implements JoinUseCase {

    private final MessageUtil messageUtil;
    private final ModelMapper modelMapper;
    private final JoinRepository joinRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final UserUtil userUtil;
    private final NotiUtil notiUtil;
    private final SmtpUtil smtpUtil;

    @Override
    @Transactional
    public UserDomain join(UserDomain userDomain) throws Exception {
        // 유효성 검사
        validateUserId(userDomain.getUserId());
        validatePassword(userDomain.getPassword());
        validateUsername(userDomain.getUsername());
        validateEmail(userDomain.getEmail());
        validateProfileImg(userDomain.getProfileImg());

        // 아이디 중복 검사
        if (joinRepository.existsByUserId(userDomain.getUserId())) {
            throw new CmcException("USER007");
        }
        // 닉네임 중복 검사
        if (joinRepository.existsByUsername(userDomain.getUsername())) {
            throw new CmcException("USER009");
        }
        // 이메일 중복 검사
        if (joinRepository.existsByEmail(userDomain.getEmail())) {
            throw new CmcException("USER019");
        }

        // 암호화 비밀번호, 프로필 디폴트 이미지 저장
        UserDomain encryptedUserDomain = userDomain.toBuilder()
                .password(passwordEncoder.encode(userDomain.getPassword()))
                .profileImg(PROFILE_IMG_BASE_URL + "default_profile.png")
                .build();

        // 회원 생성
        joinRepository.save(modelMapper.map(encryptedUserDomain, User.class));

        // 가입 이메일 전송
        final User joinUser = userRepository.findByUsername(encryptedUserDomain.getUsername())
                .orElseThrow(() -> new CmcException("USER001"));

        smtpUtil.sendEmailJoin(userDomain.getEmail(), userDomain.getUsername(), userDomain.getUserId(), userDomain.getPassword());

        Map<String, String> templateParams = Map.of("userNm", userDomain.getUsername());
        notiUtil.sendNotice( joinUser.getUserNum(), 1L, "", templateParams);

        return encryptedUserDomain.toBuilder()
                .resultMessage(messageUtil.getFormattedMessage("USER011"))
                .build();
    }

    @Override
    public UserDomain joinGoogle(UserDomain userDomain) throws Exception {
        // 유효성 검사
        validateUserId(userDomain.getUserId());
        validateUsername(userDomain.getUsername());
        validateEmail(userDomain.getEmail());
        validateProfileImg(userDomain.getProfileImg());

        // 아이디 중복 검사
        if (joinRepository.existsByUserId(userDomain.getUserId())) {
            throw new CmcException("USER007");
        }
        // 닉네임 중복 검사
        if (joinRepository.existsByUsername(userDomain.getUsername())) {
            throw new CmcException("USER009");
        }
        // 이메일 중복 검사
        if (joinRepository.existsByEmail(userDomain.getEmail())) {
            throw new CmcException("USER019");
        }

        // 암호화 비밀번호, 프로필 디폴트 이미지 저장 (랜덤 비밀번호)
        UserDomain encryptedUserDomain = userDomain.toBuilder()
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .profileImg(PROFILE_IMG_BASE_URL + "default_profile.png")
                .build();

        // 회원 생성
        joinRepository.save(modelMapper.map(encryptedUserDomain, User.class));

        return encryptedUserDomain.toBuilder()
                .resultMessage(messageUtil.getFormattedMessage("USER011"))
                .build();
    }

    @Override
    public String checkUserId(String userId) throws Exception {
        validateUserId(userId);
        return messageUtil.getFormattedMessage(joinRepository.existsByUserId(userId) ? "USER007" : "USER008");
    }

    @Override
    public String checkUsername(String username) throws Exception {
        validateUsername(username);
        return messageUtil.getFormattedMessage(joinRepository.existsByUsername(username) ? "USER009" : "USER010");
    }
}
