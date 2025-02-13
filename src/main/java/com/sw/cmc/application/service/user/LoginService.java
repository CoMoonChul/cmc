package com.sw.cmc.application.service.user;

import com.sw.cmc.adapter.in.user.dto.TempLoginResponse;
import com.sw.cmc.adapter.in.user.dto.User;
import com.sw.cmc.adapter.out.user.persistence.LoginRepository;
import com.sw.cmc.application.port.in.user.LoginUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.LoginUtil;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.domain.user.TempLogin;
import com.sw.cmc.domain.user.Token;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * packageName    : com.sw.cmc.application.service
 * fileName       : LoginService
 * author         : SungSuHan
 * date           : 2025-02-10
 * description    :
 */
@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final LoginUtil loginUtil;
    private final MessageUtil messageUtil;
    private final ModelMapper modelMapper;
    private final LoginRepository loginRepository;

    @Override
    public TempLoginResponse tempLogin(final TempLogin tempLogin) throws Exception {
        // 회원 조회
        final TempLogin tempLoginInfo = modelMapper.map(loginRepository.findByUserId(tempLogin.getUserId())
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("USER001"))), TempLogin.class);

        // 관리자 확인
        if (!Objects.equals(tempLoginInfo.getUserRole(), "ADMIN")) {
            throw new CmcException(messageUtil.getFormattedMessage("USER002"));
        }

        // 토큰 생성
        Token token = loginUtil.createToken(tempLoginInfo.getUserNum());

        // User 엔티티
        final User user = modelMapper.map(tempLoginInfo, User.class);

        // Refresh Token 저장
        user.setRefreshToken(token.getRefreshToken());

        // DB 저장
        loginRepository.save(user);

        return modelMapper.map(token, TempLoginResponse.class);
    }
}
