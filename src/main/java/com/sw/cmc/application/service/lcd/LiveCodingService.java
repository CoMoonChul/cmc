package com.sw.cmc.application.service.lcd;

import com.sw.cmc.adapter.out.lcd.persistence.LiveCodingRepository;
import com.sw.cmc.application.port.in.lcd.DeleteLcdCase;
import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.jwt.JwtToken;
import com.sw.cmc.common.jwt.JwtTokenProvider;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * packageName    : com.sw.cmc.application.service
 * fileName       : LiveCodingService
 * author         : Ko
 * date           : 2025-02-08
 * description    : LiveCoding service
 */
@Service
@RequiredArgsConstructor
public class LiveCodingService implements LiveCodingUseCase {

    private final LiveCodingRepository liveCodingRepository;  // LiveCodingRepository 주입
    private final JwtTokenProvider jwtTokenProvider;

    private static final long INVITE_EXPIRATION = 10 * 60 * 1000; // 10분


    @Override
    public LiveCodingDomain createLiveCoding(Long hostId)   {

        if (liveCodingRepository.existsByHostId(hostId)) {
            throw new CmcException("LCD003");
        }

        // 방 ID 생성 (UUID)
        UUID roomId = UUID.randomUUID();

        LocalDateTime createdAt = LocalDateTime.now();

        // 초기 참가자 목록 (방장만 포함)
        List<Long> participants = new ArrayList<>();
        participants.add(hostId);

        // 참가자 수 초기화 (방장 포함)
        Integer participantCount = 1;

        // LiveCodingDomain 객체 생성
        LiveCodingDomain liveCodingDomain = new LiveCodingDomain(
                roomId,  // 생성된 방 ID
                hostId,  // 방장 ID
                createdAt,  // 방 생성 시간
                participantCount,  // 참가자 수
                participants  // 참가자 목록
        );

        // Redis에 방 정보 저장
        liveCodingRepository.saveLiveCoding(liveCodingDomain);  // Repository 사용

        return liveCodingDomain;  // 생성된 LiveCodingDomain 반환
    }

    @Override
    public DeleteLcdCase deleteLiveCoding(UUID roomId) {
        boolean isDeleted = liveCodingRepository.deleteLiveCoding(roomId);
        if (isDeleted) {
            return DeleteLcdCase.SUCCESS;
        } else {
            return DeleteLcdCase.FAIL;
        }
    }

    @Override
    public String generateInviteLink(UUID roomId) throws Exception {
        // roomId를 클레임에 포함시켜 JWT 생성
        final Claims claims = Jwts.claims();
        claims.put("roomId", roomId.toString());

        // 만료 시간을 INVITE_EXPIRATION으로 설정
        // JWT 토큰 생성 시 만료 시간을 포함시켜 토큰을 생성하는 부분만 수정
        JwtToken jwtToken = jwtTokenProvider.createToken(claims);  // 기본 createToken을 사용

        // AccessToken 만료 시간을 INVITE_EXPIRATION으로 설정
//        jwtToken.setAccessTokenExpirationTime(INVITE_EXPIRATION);  // 수정된 만료 시간을 적용 (서비스 내에서 처리)

        // 생성된 JWT 토큰을 포함한 초대 링크 반환
        return  "https://cmc.com/livecoding/join?token=" + jwtToken.getAccessToken();
    }

    @Override
    public LiveCodingDomain selectLiveCoding(UUID roomId) throws CmcException {
        LiveCodingDomain liveCodingDomain = liveCodingRepository.findByRoomId(roomId);
        if (liveCodingDomain == null) {
            throw new CmcException("LCD001");
        }
        return liveCodingDomain;
    }

    @Override
    public LiveCodingDomain updateLiveCoding(UUID roomId, Long userNum, String action) throws CmcException {
        LiveCodingDomain liveCodingDomain = liveCodingRepository.findByRoomId(roomId);
        if (liveCodingDomain == null) {
            throw new CmcException("LCD001");
        }

        // 참가 또는 나가기 처리
        if ("JOIN".equals(action)) {
            liveCodingDomain.joinParticipant(userNum);
        } else if ("LEAVE".equals(action)) {
            liveCodingDomain.leaveParticipant(userNum);
        } else {
            throw new CmcException("LCD002");
        }

        // Redis에 업데이트된 방 정보 저장
        liveCodingRepository.saveLiveCoding(liveCodingDomain);

        return liveCodingDomain;
    }
}
