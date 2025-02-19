package com.sw.cmc.application.service.user;

import com.sw.cmc.adapter.in.user.dto.LoginResDTO;
import com.sw.cmc.adapter.in.user.dto.TempLoginResDTO;
import com.sw.cmc.entity.User;
import com.sw.cmc.adapter.out.user.persistence.LoginRepository;
import com.sw.cmc.application.port.in.user.LoginUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.LoginUtil;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.domain.user.LoginDomain;
import com.sw.cmc.domain.user.TempLoginDomain;
import com.sw.cmc.domain.user.Token;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final AuthenticationManager authenticationManager;

    @Override
    public TempLoginResDTO tempLogin(final TempLoginDomain tempLoginDomain) throws Exception {
        // 회원 조회
        final TempLoginDomain tempLoginDomainInfo = modelMapper.map(loginRepository.findByUserId(tempLoginDomain.getUserId())
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("USER001"))), TempLoginDomain.class);

        // 관리자 확인
        if (!Objects.equals(tempLoginDomainInfo.getUserRole(), "ADMIN")) {
            throw new CmcException(messageUtil.getFormattedMessage("USER002"));
        }

        // 토큰 생성
        Token token = loginUtil.createToken(tempLoginDomainInfo.getUserNum(), tempLoginDomainInfo.getUserId());

        // User 엔티티
        final User user = modelMapper.map(tempLoginDomainInfo, User.class);

        // Refresh Token 저장
        user.setRefreshToken(token.getRefreshToken());

        // DB 저장
        loginRepository.save(user);

        return modelMapper.map(token, TempLoginResDTO.class);
    }

    @Override
    public LoginResDTO login(final LoginDomain loginDomain) throws Exception {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDomain.getUserId(), loginDomain.getPassword())
        );

        // 회원 조회
        final LoginDomain loginDomainInfo = modelMapper.map(loginRepository.findByUserId(loginDomain.getUserId()), LoginDomain.class);

        // 토큰 생성
        Token token = loginUtil.createToken(loginDomainInfo.getUserNum(), loginDomainInfo.getUserId());

        // User 엔티티
        final User user = modelMapper.map(loginDomainInfo, User.class);

        // Refresh Token 저장
        user.setRefreshToken(token.getRefreshToken());

        // DB 저장
        loginRepository.save(user);

        return modelMapper.map(token, LoginResDTO.class);
    }
}
