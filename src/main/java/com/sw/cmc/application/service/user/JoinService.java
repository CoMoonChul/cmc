package com.sw.cmc.application.service.user;

import com.sw.cmc.adapter.in.user.dto.JoinCheckResponse;
import com.sw.cmc.adapter.in.user.dto.JoinResponse;
import com.sw.cmc.adapter.in.user.dto.User;
import com.sw.cmc.adapter.out.user.persistence.JoinRepository;
import com.sw.cmc.application.port.in.user.JoinUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.domain.user.Join;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sw.cmc.domain.user.Join.*;

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

    @Override
    @Transactional
    public JoinResponse join(Join join) throws Exception {
        // 유효성 검사
        validateUserId(join.getUserId(), messageUtil.getFormattedMessage("USER003"));
        validatePassword(join.getPassword(), messageUtil.getFormattedMessage("USER004"));
        validateEmail(join.getEmail(), messageUtil.getFormattedMessage("USER005"));
        validateUsername(join.getUsername(), messageUtil.getFormattedMessage("USER006"));

        // 아이디 중복 검사
        if (joinRepository.existsByUserId(join.getUserId())) {
            throw new CmcException(messageUtil.getFormattedMessage("USER007"));
        }
        // 닉네임 중복 검사
        if (joinRepository.existsByUsername(join.getUsername())) {
            throw new CmcException(messageUtil.getFormattedMessage("USER009"));
        }

        // 회원 생성
        joinRepository.save(modelMapper.map(join, User.class));

        return new JoinResponse().resultMessage(messageUtil.getFormattedMessage("USER011"));
    }

    @Override
    public JoinCheckResponse checkUserId(String userId) throws Exception {
        validateUserId(userId, messageUtil.getFormattedMessage("USER003"));

        return new JoinCheckResponse()
            .resultMessage(messageUtil.getFormattedMessage(
                joinRepository.existsByUserId(userId) ? "USER007" : "USER008"
            ));
    }

    @Override
    public JoinCheckResponse checkUsername(String username) throws Exception {
        validateUsername(username, messageUtil.getFormattedMessage("USER006"));

        return new JoinCheckResponse()
            .resultMessage(messageUtil.getFormattedMessage(
                joinRepository.existsByUsername(username) ? "USER009" : "USER010"
            ));
    }
}
