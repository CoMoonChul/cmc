package com.sw.cmc.application.service.user;

import com.sw.cmc.adapter.in.user.dto.*;
import com.sw.cmc.adapter.out.user.persistence.LoginRepository;
import com.sw.cmc.application.port.in.user.LoginUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.filter.JwtAuthenticationFilter;
import com.sw.cmc.common.jwt.JwtToken;
import com.sw.cmc.common.jwt.JwtTokenProvider;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.user.TokenDomain;
import com.sw.cmc.domain.user.UserDomain;
import com.sw.cmc.entity.User;
import com.sw.cmc.event.notice.SendNotiEmailEvent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sw.cmc.domain.user.UserDomain.*;

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

    private final UserUtil userUtil;
    private final ModelMapper modelMapper;
    private final MessageUtil messageUtil;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    @Transactional
    public TempLoginResDTO tempLogin(final UserDomain userDomain) throws Exception {
        // 회원 조회
        final UserDomain userInfo = modelMapper.map(loginRepository.findByUserId(userDomain.getUserId())
                .orElseThrow(() -> new CmcException("USER001")), UserDomain.class);

        // 관리자 확인
        if (!Objects.equals(userInfo.getUserRole(), "ADMIN")) {
            throw new CmcException("USER002");
        }

        // Token 생성
        JwtToken jwtToken = userUtil.createToken(userInfo.getUserNum(), userInfo.getUserId());

        // User 엔티티
        final User user = modelMapper.map(userInfo, User.class);

        // RefreshToken 저장
        user.setRefreshToken(jwtToken.getRefreshToken());

        // DB 저장
        loginRepository.save(user);

        return modelMapper.map(jwtToken, TempLoginResDTO.class);
    }

    @Override
    @Transactional
    public LoginResDTO login(final UserDomain userDomain) throws Exception {
        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDomain.getUserId(), userDomain.getPassword())
        );

        // 회원 조회
        final UserDomain userInfo = modelMapper.map(loginRepository.findByUserId(userDomain.getUserId()), UserDomain.class);

        // 토큰 생성
        JwtToken jwtToken = userUtil.createToken(userInfo.getUserNum(), userInfo.getUserId());

        // RefreshToken 암호화
        final TokenDomain tokenDomain = modelMapper.map(jwtToken, TokenDomain.class);
        tokenDomain.setRefreshToken(userUtil.encrypt(jwtToken.getRefreshToken()));

        // User 엔티티
        final User user = modelMapper.map(userInfo, User.class);

        // RefreshToken 저장
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
            throw new CmcException("USER013");
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

    @Override
    @Transactional
    public LogoutResDTO logout(HttpServletRequest request) throws Exception {
        // AccessToken 추출
        String accessToken = jwtAuthenticationFilter.getTokenFromRequest(request);

        // 회원 번호
        Long userNum = jwtTokenProvider.getClaims(accessToken).get("userNum", Long.class);

        // 회원 조회
        final User user = modelMapper.map(loginRepository.findByUserNum(userNum)
                .orElseThrow(() -> new CmcException("USER001")), User.class);

        // RefreshToken 세팅
        user.setRefreshToken(null);

        // DB 저장
        loginRepository.save(user);

        return new LogoutResDTO().resultMessage(messageUtil.getFormattedMessage("USER015"));
    }

    @Override
    @Transactional
    public FindAccountResDTO findAccount(UserDomain userDomain) throws Exception {
        // 이메일
        String email = userDomain.getEmail();
        validateEmail(email);

        // DB 조회
        User user = modelMapper.map(loginRepository.findByEmail(email)
                .orElseThrow(() -> new CmcException("USER016")), User.class);

        // 회원 ID
        String userId = user.getUserId();

        // 회원명
        String username = user.getUsername();

        // 랜덤 비밀번호 생성
        String password = createRandomPassword();

        // 랜덤 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 암호화된 비밀번호를 User 객체에 설정
        user.setPassword(encodedPassword);

        // DB 저장
        loginRepository.save(user);

        // 이메일 전송
        sendEmail(email, username, userId, password);

        return new FindAccountResDTO().resultMessage(messageUtil.getFormattedMessage("USER017"));
    }

    public void sendEmail(String email, String username, String userId, String password) throws Exception {
        String template = "코문철 계정 찾기";

        SendNotiEmailEvent sendNotiEmailEvent = SendNotiEmailEvent.builder()
                .rsvrEmail(email)
                .subject(template)
                .text(
                        username + "님, 안녕하세요.\n\n" +
                        username + "님의 아이디와 새로운 비밀번호는 " + userId + " / " + password + " 입니다.\n\n" +
                        "지금 바로 로그인을 진행해 주세요."
                    )
                .build();

        eventPublisher.publishEvent(sendNotiEmailEvent);
    }
}
