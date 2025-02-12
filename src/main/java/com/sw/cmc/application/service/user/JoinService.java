package com.sw.cmc.application.service.user;

import com.sw.cmc.adapter.in.user.dto.JoinResponse;
import com.sw.cmc.adapter.in.user.dto.User;
import com.sw.cmc.adapter.out.persistence.user.JoinRepository;
import com.sw.cmc.application.port.in.user.JoinUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.domain.user.Join;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        join.validateUserId(join.getUserId(), messageUtil.getFormattedMessage("USER003"));
        join.validatePassword(join.getPassword(), messageUtil.getFormattedMessage("USER004"));
        join.validateEmail(join.getEmail(), messageUtil.getFormattedMessage("USER005"));
        join.validateUserName(join.getUserName(), messageUtil.getFormattedMessage("USER006"));

        // 아이디 중복 검사
        if (joinRepository.findByUserId(join.getUserId()).isPresent()) {
            throw new CmcException(messageUtil.getFormattedMessage("USER007"));
        }
        // 닉네임 중복 검사
        if (joinRepository.findByUserName(join.getUserName()).isPresent()) {
            throw new CmcException(messageUtil.getFormattedMessage("USER008"));
        }

        // 회원 가입 일시
        LocalDateTime now = LocalDateTime.now();
        join.setCreatedDtm(now);
        join.setUpdatedDtm(now);

        // 회원 생성
        joinRepository.save(modelMapper.map(join, User.class));

        return new JoinResponse().resultMsg(messageUtil.getFormattedMessage("USER009"));
    }
}
