package com.sw.cmc.application.service.user;

import com.sw.cmc.adapter.in.user.dto.CheckJoinResDTO;
import com.sw.cmc.adapter.in.user.dto.JoinResDTO;
import com.sw.cmc.domain.user.UserDomain;
import com.sw.cmc.entity.User;
import com.sw.cmc.adapter.out.user.persistence.JoinRepository;
import com.sw.cmc.application.port.in.user.JoinUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sw.cmc.domain.user.UserDomain.*;

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

    @Override
    @Transactional
    public JoinResDTO join(UserDomain userDomain) throws Exception {
        // 유효성 검사
        validateUserId(userDomain.getUserId());
        validatePassword(userDomain.getPassword());
        validateEmail(userDomain.getEmail());
        validateUsername(userDomain.getUsername());

        // 아이디 중복 검사
        if (joinRepository.existsByUserId(userDomain.getUserId())) {
            throw new CmcException("USER007");
        }
        // 닉네임 중복 검사
        if (joinRepository.existsByUsername(userDomain.getUsername())) {
            throw new CmcException("USER009");
        }

        String encodedPassword = passwordEncoder.encode(userDomain.getPassword());

        // 암호화된 비밀번호를 User 객체에 설정
        userDomain.setPassword(encodedPassword);

        // 회원 생성
        joinRepository.save(modelMapper.map(userDomain, User.class));

        return new JoinResDTO().resultMessage(messageUtil.getFormattedMessage("USER011"));
    }

    @Override
    public CheckJoinResDTO checkUserId(String userId) throws Exception {
        validateUserId(userId);

        return new CheckJoinResDTO()
            .resultMessage(messageUtil.getFormattedMessage(
                joinRepository.existsByUserId(userId) ? "USER007" : "USER008"
            ));
    }

    @Override
    public CheckJoinResDTO checkUsername(String username) throws Exception {
        validateUsername(username);

        return new CheckJoinResDTO()
            .resultMessage(messageUtil.getFormattedMessage(
                joinRepository.existsByUsername(username) ? "USER009" : "USER010"
            ));
    }
}
