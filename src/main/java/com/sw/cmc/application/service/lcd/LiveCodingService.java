package com.sw.cmc.application.service.lcd;

import com.sw.cmc.adapter.out.lcd.persistence.RedisRepository;
import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.jwt.JwtToken;
import com.sw.cmc.common.jwt.JwtTokenProvider;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.lcd.LiveCodingAction;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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


    private final JwtTokenProvider jwtTokenProvider;
    private final RedisRepository redisRepository;
    private final StringRedisTemplate redisTemplate;
    private final UserUtil userUtil;

    private static final String REDIS_LIVE_CODING_PREFIX = "live_coding:";  // Redis에 저장할 키 접두사

    @Override
    public LiveCodingDomain createLiveCoding(Long hostId) throws CmcException {

        Long currentUser = userUtil.getAuthenticatedUserNum();
        if (!Objects.equals(currentUser, hostId)) {
            throw new CmcException("LCD008");
        }

        if (this.existsByHostId(hostId)) {
            throw new CmcException("LCD003");
        }

        UUID roomId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        List<Long> participants = new ArrayList<>();
        participants.add(hostId);
        Integer participantCount = 1;
        String link = this.generateInviteLink(roomId);

        // LiveCodingDomain 객체 생성
        LiveCodingDomain liveCodingDomain = new LiveCodingDomain(
                roomId,  // 생성된 방 ID
                hostId,  // 방장 ID
                createdAt,  // 방 생성 시간
                participantCount,  // 참가자 수
                participants,  // 참가자 목록
                link   // 링크
        );

        // Redis에 방 정보 저장
        this.saveLiveCoding(liveCodingDomain);  // Repository 사용
        this.saveLiveCodeSnippet(liveCodingDomain);

        return liveCodingDomain;  // 생성된 LiveCodingDomain 반환
    }

    @Override
    public boolean deleteLiveCoding(UUID roomId) {
        String key = REDIS_LIVE_CODING_PREFIX + roomId;  // 방 정보 저장 키
        Map<String, String> liveCodingMap = redisRepository.selectHash(key);  // 방 정보 조회
        if (liveCodingMap == null || liveCodingMap.isEmpty()) {
            throw new CmcException("LCD001");
        }

        String hostIdKey = REDIS_LIVE_CODING_PREFIX + "host:" + liveCodingMap.get("hostId");
        redisRepository.delete(hostIdKey);  // 호스트 ID 기반 매핑 삭제
        return redisRepository.delete(key);  // Redis에서 해당 key 삭제
    }

    @Override
    public String generateInviteLink(UUID roomId)  {
        final Claims claims = Jwts.claims();
        claims.put("roomId", roomId.toString());
        JwtToken jwtToken = jwtTokenProvider.createLcdToken(claims);
        return "http://localhost:3000/livecoding/join?token=" + jwtToken.getAccessToken();
    }

    @Override
    public LiveCodingDomain verifyLiveCoding(String token)  {
        UUID verifiedRoomID = jwtTokenProvider.validateLcdToken(token);
        return this.updateLiveCoding(verifiedRoomID, userUtil.getAuthenticatedUserNum(), LiveCodingAction.JOIN.getAction());
    }


    @Override
    public LiveCodingDomain selectLiveCoding(UUID roomId) throws CmcException {
        LiveCodingDomain liveCodingDomain = this.findByRoomId(roomId);
        if (liveCodingDomain == null) {
            throw new CmcException("LCD001");
        }
        return liveCodingDomain;
    }

    @Override
    public LiveCodingDomain updateLiveCoding(UUID roomId, Long userNum, int action) throws CmcException {
        LiveCodingDomain liveCodingDomain = this.findByRoomId(roomId);
        if (liveCodingDomain == null) {
            throw new CmcException("LCD001");
        }
        if (action == LiveCodingAction.JOIN.getAction()) {

            if (liveCodingDomain.getParticipants().contains(userNum)) {
                throw new CmcException("LCD005");
            }

            liveCodingDomain.joinParticipant(userNum);
        } else if (action == LiveCodingAction.LEAVE.getAction()) {
            liveCodingDomain.leaveParticipant(userNum);
        } else {
            throw new CmcException("LCD002");
        }

        // Redis에 업데이트된 방 정보 저장
        this.saveLiveCoding(liveCodingDomain);

        return liveCodingDomain;
    }

    @Override
    public UUID findRoomIdByHostId(Long hostId) {
        String roomIdStr = redisRepository.select(REDIS_LIVE_CODING_PREFIX + "host:" + hostId);
        return roomIdStr != null ? UUID.fromString(roomIdStr) : null;
    }

    @Override
    public boolean existsByHostId(Long hostId) {
        return findRoomIdByHostId(hostId) != null;
    }

    @Override
    public LiveCodingDomain findByRoomId(UUID roomId) {
        String key = REDIS_LIVE_CODING_PREFIX + roomId;
        Map<String, String> liveCodingMap = redisRepository.selectHash(key);  // 수정된 부분: Map<String, String>으로 처리

        if (liveCodingMap == null || liveCodingMap.isEmpty()) {
            return null;  // 방이 없으면 null 반환
        }

        UUID retrievedRoomId = UUID.fromString(liveCodingMap.get("roomId"));
        Long hostId = Long.valueOf(liveCodingMap.get("hostId"));

        String createdAtStr = liveCodingMap.get("createdAt");
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime createdAt = LocalDateTime.parse(createdAtStr, formatter);

        Integer participantCount = Integer.valueOf(liveCodingMap.get("participantCount"));
        List<Long> participants = Arrays.stream(liveCodingMap.get("participants").split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
        String link = liveCodingMap.get("link");

        return new LiveCodingDomain(retrievedRoomId, hostId, createdAt, participantCount, participants, link);
    }


    @Override
    public void saveLiveCoding(LiveCodingDomain liveCodingDomain) {
        Map<String, String> liveCodingMap = new HashMap<>();
        liveCodingMap.put("roomId", liveCodingDomain.getRoomId().toString());
        liveCodingMap.put("hostId", liveCodingDomain.getHostId().toString());
        liveCodingMap.put("createdAt", String.valueOf(liveCodingDomain.getCreatedAt()));
        liveCodingMap.put("participantCount", liveCodingDomain.getParticipantCount().toString());
        liveCodingMap.put("participants", String.join(",", liveCodingDomain.getParticipants().stream().map(String::valueOf).toArray(String[]::new)));
        liveCodingMap.put("link", liveCodingDomain.getLink());

        redisRepository.saveHash(REDIS_LIVE_CODING_PREFIX + liveCodingDomain.getRoomId().toString(), liveCodingMap);
        redisTemplate.expire(REDIS_LIVE_CODING_PREFIX + liveCodingDomain.getRoomId().toString(), 1, TimeUnit.HOURS);
    }

    private boolean isHost(LiveCodingDomain liveCodingDomain) {
        Long authenticatedUserNum = userUtil.getAuthenticatedUserNum();
        return authenticatedUserNum != null && authenticatedUserNum.equals(liveCodingDomain.getHostId());
    }

    private void saveLiveCodeSnippet(LiveCodingDomain liveCodingDomain) {
        redisRepository.save(REDIS_LIVE_CODING_PREFIX + "host:" + liveCodingDomain.getHostId(), liveCodingDomain.getRoomId().toString());
        redisTemplate.expire(REDIS_LIVE_CODING_PREFIX + "host:" + liveCodingDomain.getHostId(), 1, TimeUnit.HOURS);
    }

}
