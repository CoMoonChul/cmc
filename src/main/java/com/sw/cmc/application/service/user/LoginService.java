package com.sw.cmc.application.service.user;

import com.sw.cmc.adapter.in.user.dto.LoginResDTO;
import com.sw.cmc.adapter.in.user.dto.RefreshResDTO;
import com.sw.cmc.adapter.in.user.dto.TempLoginResDTO;
import com.sw.cmc.common.jwt.JwtTokenProvider;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.entity.User;
import com.sw.cmc.adapter.out.user.persistence.LoginRepository;
import com.sw.cmc.application.port.in.user.LoginUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.domain.user.UserDomain;
import com.sw.cmc.domain.user.TokenDomain;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.sw.cmc.domain.user.TokenDomain.encryptToken;

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

    private final UserUtil userUtil;
    private final MessageUtil messageUtil;
    private final ModelMapper modelMapper;
    private final LoginRepository loginRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public TempLoginResDTO tempLogin(final UserDomain userDomain) throws Exception {
        // 회원 조회
        final UserDomain userInfo = modelMapper.map(loginRepository.findByUserId(userDomain.getUserId())
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("USER001"))), UserDomain.class);

        // 관리자 확인
        if (!Objects.equals(userInfo.getUserRole(), "ADMIN")) {
            throw new CmcException(messageUtil.getFormattedMessage("USER002"));
        }

        // Token 생성
        TokenDomain tokenDomain = userUtil.createToken(userInfo.getUserNum(), userInfo.getUserId());

        // User 엔티티
        final User user = modelMapper.map(userInfo, User.class);

        // RefreshToken 암호화 저장
        user.setRefreshToken(tokenDomain.getRefreshToken());

        // DB 저장
        loginRepository.save(user);

        return modelMapper.map(tokenDomain, TempLoginResDTO.class);
    }

    @Override
    public LoginResDTO login(final UserDomain userDomain) throws Exception {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDomain.getUserId(), userDomain.getPassword())
        );

        // 회원 조회
        final UserDomain userInfo = modelMapper.map(loginRepository.findByUserId(userDomain.getUserId()), UserDomain.class);

        // 토큰 생성
        TokenDomain tokenDomain = userUtil.createToken(userInfo.getUserNum(), userInfo.getUserId());

        tokenDomain = encryptToken(tokenDomain, userUtil);

        // User 엔티티
        final User user = modelMapper.map(userInfo, User.class);

        // Refresh Token 저장
        user.setRefreshToken(tokenDomain.getRefreshToken());

        // DB 저장
        loginRepository.save(user);

        return modelMapper.map(tokenDomain, LoginResDTO.class);
    }

    @Override
    public RefreshResDTO refresh(HttpServletRequest request) throws Exception {
        // 쿠키에서 RefreshToken 복호화 추출
        String refreshToken = userUtil.decrypt(userUtil.getRefreshTokenFromCookie(request));

        // RefreshToken 유효성 검사
        if (StringUtils.isEmpty(refreshToken) || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new CmcException(messageUtil.getFormattedMessage("USER013"));
        }

        // 회원 번호
        Long userNum = jwtTokenProvider.getClaims(refreshToken).get("userNum", Long.class);

        // 회원 조회
        final UserDomain userInfo = modelMapper.map(loginRepository.findByUserNum(userNum), UserDomain.class);

        // AccessToken 재발급
        RefreshResDTO refreshResDTO = new RefreshResDTO();
        refreshResDTO.setAccessToken(userUtil.createAccessToken(userInfo.getUserNum(), userInfo.getUserId()));

        return refreshResDTO;
    }
}
