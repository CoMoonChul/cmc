package com.sw.cmc.application.service;

import com.sw.cmc.adapter.in.user.dto.TempLoginResponse;
import com.sw.cmc.application.port.in.LoginUseCase;
import com.sw.cmc.domain.TempLogin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    private final ModelMapper modelMapper;

    @Override
    public TempLoginResponse tempLogin(final TempLogin tempLogin) throws Exception {
        // 여기서 인풋 빈값 확인
        // 여기서 회원 db 조회
        // 여기서 회원 code 확인

        // 여기부터 로그인 유틸 안에 넣을 꺼임 (createToken 이런식으로 뺄꺼임ㅇㅇ)



        return null;
    }
}
