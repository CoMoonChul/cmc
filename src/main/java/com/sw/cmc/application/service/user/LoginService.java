package com.sw.cmc.application.service.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.sw.cmc.adapter.out.user.persistence.LoginRepository;
import com.sw.cmc.application.port.in.user.LoginUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.filter.JwtAuthenticationFilter;
import com.sw.cmc.common.jwt.JwtToken;
import com.sw.cmc.common.jwt.JwtTokenProvider;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.common.util.SmtpUtil;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.user.TokenDomain;
import com.sw.cmc.domain.user.UserDomain;
import com.sw.cmc.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;

import static com.sw.cmc.domain.user.UserDomain.createRandomPassword;
import static com.sw.cmc.domain.user.UserDomain.validateEmail;

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
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SmtpUtil smtpUtil;

    @Value("${google.client-id}")
    private String googleClientId;

    @Override
    @Transactional
    public UserDomain tempLogin(final UserDomain userDomain) throws Exception {
        // 회원 조회
        final User user = loginRepository.findByUserId(userDomain.getUserId())
                .orElseThrow(() -> new CmcException("USER001"));

        // 관리자 확인
        if (!Objects.equals(user.getUserRole(), "ADMIN")) {
            throw new CmcException("USER002");
        }

        // Token 생성
        final JwtToken jwtToken = userUtil.createToken(user.getUserNum(), user.getUsername(), user.getUserId());

        // RefreshToken 저장
        user.setRefreshToken(jwtToken.getRefreshToken());

        // DB 저장
        loginRepository.save(user);

        return UserDomain.builder()
                    .accessToken(jwtToken.getAccessToken())
                    .accessTokenExpirationTime(jwtToken.getAccessTokenExpirationTime())
                    .refreshToken(jwtToken.getRefreshToken())
                    .refreshTokenExpirationTime(jwtToken.getRefreshTokenExpirationTime())
                    .build();
    }

    @Override
    @Transactional
    public UserDomain login(final UserDomain userDomain) throws Exception {
        // 회원 조회
        final User user = loginRepository.findByUserId(userDomain.getUserId())
                .orElseThrow(() -> new CmcException("USER034"));

        // 비밀번호 검사
        boolean isMatch = passwordEncoder.matches(userDomain.getPassword(), user.getPassword());
        if (!isMatch) {
            throw new CmcException("USER029");
        }

        // 토큰 생성
        final JwtToken jwtToken = userUtil.createToken(user.getUserNum(), user.getUsername(), user.getUserId());

        // RefreshToken 암호화
        final TokenDomain tokenDomain = modelMapper.map(jwtToken, TokenDomain.class);
        tokenDomain.setRefreshToken(userUtil.encrypt(jwtToken.getRefreshToken()));

        // RefreshToken 저장
        user.setRefreshToken(tokenDomain.getRefreshToken());

        // DB 저장
        loginRepository.save(user);

        return UserDomain.builder()
                .accessToken(tokenDomain.getAccessToken())
                .refreshToken(tokenDomain.getRefreshToken())
                .userNum(user.getUserNum())
                .build();
    }

    @Override
    @Transactional
    public UserDomain loginGoogle(UserDomain userDomain) throws Exception {
        String idTokenString = userDomain.getIdToken();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance()
        )
        .setAudience(Collections.singletonList(googleClientId))
        .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken == null) {
            throw new CmcException("USER036");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();


        final User user = loginRepository.findByEmail(email)
                .orElseThrow(() -> new CmcException("USER034"));

        // 토큰 생성
        final JwtToken jwtToken = userUtil.createToken(user.getUserNum(), user.getUsername(), user.getUserId());

        // RefreshToken 암호화
        final TokenDomain tokenDomain = modelMapper.map(jwtToken, TokenDomain.class);
        tokenDomain.setRefreshToken(userUtil.encrypt(jwtToken.getRefreshToken()));

        // RefreshToken 저장
        user.setRefreshToken(tokenDomain.getRefreshToken());

        // DB 저장
        loginRepository.save(user);

        return UserDomain.builder()
                .accessToken(tokenDomain.getAccessToken())
                .refreshToken(tokenDomain.getRefreshToken())
                .userNum(user.getUserNum())
                .build();
    }

    @Override
    public String refresh(HttpServletRequest request) throws Exception {
        // 쿠키에서 RefreshToken 복호화 추출
        String refreshToken = userUtil.decrypt(userUtil.getRefreshTokenFromCookie(request));

        // RefreshToken 유효성 검사
        if (StringUtils.isEmpty(refreshToken) || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new CmcException("USER013");
        }

        // 회원 번호
        Long userNum = jwtTokenProvider.getClaims(refreshToken).get("userNum", Long.class);

        // 회원 조회
        final User user = loginRepository.findByUserNum(userNum).orElseThrow(() -> new CmcException("USER001"));

        // AccessToken 재발급
        return userUtil.createAccessToken(user.getUserNum(), user.getUsername(), user.getUserId());
    }

    public String tempRefresh(String refreshToken) throws Exception {

        String decrypted = userUtil.decrypt(refreshToken);

        // RefreshToken 유효성 검사
        if (StringUtils.isEmpty(decrypted) || !jwtTokenProvider.validateToken(decrypted)) {
            throw new CmcException("USER013");
        }

        // 회원 번호
        Long userNum = jwtTokenProvider.getClaims(decrypted).get("userNum", Long.class);

        // 회원 조회
        final User user = loginRepository.findByUserNum(userNum).orElseThrow(() -> new CmcException("USER001"));

        // AccessToken 재발급
        return userUtil.createAccessToken(user.getUserNum(), user.getUsername(), user.getUserId());
    }

    @Override
    @Transactional
    public String logout(HttpServletRequest request) throws Exception {
        // AccessToken 추출
        String accessToken = jwtAuthenticationFilter.getTokenFromRequest(request);

        // 회원 번호
        Long userNum = jwtTokenProvider.getClaims(accessToken).get("userNum", Long.class);

        // 회원 조회
        final User user = loginRepository.findByUserNum(userNum).orElseThrow(() -> new CmcException("USER001"));

        // RefreshToken 세팅
        user.setRefreshToken(null);

        // DB 저장
        loginRepository.save(user);

        return messageUtil.getFormattedMessage("USER015");
    }

    @Override
    @Transactional
    public String findAccount(UserDomain userDomain) throws Exception {
        // 이메일
        String email = userDomain.getEmail();
        validateEmail(email);

        // 회원 조회
        final User user = loginRepository.findByEmail(email).orElseThrow(() -> new CmcException("USER016"));

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
        smtpUtil.sendEmailFindAccount(email, username, userId, password);

        return messageUtil.getFormattedMessage("USER017");
    }


}
