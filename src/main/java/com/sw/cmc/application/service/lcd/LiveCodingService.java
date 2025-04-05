package com.sw.cmc.application.service.lcd;

import com.sw.cmc.adapter.in.lcd.web.WebSocketBroadcaster;
import com.sw.cmc.adapter.in.livecoding.dto.UpdateLiveCodingSnippetReqDTO;
import com.sw.cmc.adapter.in.livecoding.dto.UpdateLiveCodingSnippetResDTO;
import com.sw.cmc.adapter.out.lcd.persistence.RedisRepository;
import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.jwt.JwtToken;
import com.sw.cmc.common.jwt.JwtTokenProvider;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.lcd.LiveCodeSnippetDomain;
import com.sw.cmc.domain.lcd.LiveCodingAction;
import com.sw.cmc.domain.lcd.LiveCodingConstants;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.*;
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
    private final WebSocketBroadcaster webSocketBroadcaster;


    private static final String LCD_PREFIX = LiveCodingConstants.LCD_PREFIX;
    private static final String LCD_CODE_PREFIX = LiveCodingConstants.LCD_CODE_PREFIX;
    private static final String LCD_CODE_INIT = LiveCodingConstants.LCD_CODE_INIT;

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
        String key = LCD_PREFIX + roomId;  // 방 정보 저장 키

        Map<String, String> liveCodingMap = redisRepository.selectHash(key);  // 방 정보 조회
        if (liveCodingMap == null || liveCodingMap.isEmpty()) {
            throw new CmcException("LCD001");
        }

        String codeSnippetKey = LCD_CODE_PREFIX + liveCodingMap.get("hostId");
        redisRepository.delete(codeSnippetKey);  // 호스트 ID 기반 매핑 삭제
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
        String roomIdStr = redisRepository.select(LCD_CODE_PREFIX + hostId);
        return roomIdStr != null ? UUID.fromString(roomIdStr) : null;
    }

    @Override
    public boolean existsByHostId(Long hostId) {
        return findRoomIdByHostId(hostId) != null;
    }

    @Override
    public LiveCodingDomain findByRoomId(UUID roomId) {
        String key = LCD_PREFIX + roomId;
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
        String redisKey = LCD_PREFIX + liveCodingDomain.getRoomId().toString();
        Map<String, String> liveCodingMap = new HashMap<>();
        liveCodingMap.put("roomId", liveCodingDomain.getRoomId().toString());
        liveCodingMap.put("hostId", liveCodingDomain.getHostId().toString());
        liveCodingMap.put("createdAt", String.valueOf(liveCodingDomain.getCreatedAt()));
        liveCodingMap.put("participantCount", liveCodingDomain.getParticipantCount().toString());
        liveCodingMap.put("participants", String.join(",", liveCodingDomain.getParticipants().stream().map(String::valueOf).toArray(String[]::new)));
        liveCodingMap.put("link", liveCodingDomain.getLink());

        redisRepository.saveHash(redisKey, liveCodingMap);
        redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
    }

    @Override
    public LiveCodeSnippetDomain selectLiveCodingSnippet(Long hostId) {
        String redisKey = LCD_CODE_PREFIX + hostId;
        Map<String, String> liveCodeMap = redisRepository.selectHash(redisKey);

        if (liveCodeMap == null || liveCodeMap.isEmpty()) {
            return null;
        }

        int line = parseSafeInt(liveCodeMap.get("cursorPos.line"), 0);
        int ch = parseSafeInt(liveCodeMap.get("cursorPos.ch"), 0);

        LocalDateTime lastModified = LocalDateTime.ofInstant(
                Instant.parse(liveCodeMap.get("lastModified")),
                ZoneId.systemDefault()
        );

        return new LiveCodeSnippetDomain(
                UUID.fromString(liveCodeMap.get("roomId")),
                liveCodeMap.get("code"),
                new LiveCodeSnippetDomain.Diff(
                        parseSafeInt(liveCodeMap.get("diff.start"), 0),
                        parseSafeInt(liveCodeMap.get("diff.length"), 0),
                        liveCodeMap.getOrDefault("diff.text", "")
                ),
                liveCodeMap.get("language"),
                lastModified,
                new LiveCodeSnippetDomain.CursorPosition(line, ch),
                hostId
        );
    }

    @Override
    public UpdateLiveCodingSnippetResDTO updateLiveCodingSnippet(UpdateLiveCodingSnippetReqDTO reqDTO) {
        Long modifier = userUtil.getAuthenticatedUserNum();
        Long hostId = reqDTO.getHostId();

        // 1. 유효성 체크
        LiveCodeSnippetDomain snippetDomain = this.selectLiveCodingSnippet(hostId);
        if (snippetDomain == null) {
            throw new CmcException("LCD017"); // 존재하지 않는 방
        }

        // 2. Redis에 Diff 정보 저장
        String redisKey = LCD_CODE_PREFIX + hostId;

        redisRepository.updateHashValue(redisKey, "diff.start", String.valueOf(reqDTO.getDiff().getStart()));
        redisRepository.updateHashValue(redisKey, "diff.length", String.valueOf(reqDTO.getDiff().getLength()));
        redisRepository.updateHashValue(redisKey, "diff.text", reqDTO.getDiff().getText());
        redisRepository.updateHashValue(redisKey, "cursorPos.line", String.valueOf(reqDTO.getCursorPos().getLine()));
        redisRepository.updateHashValue(redisKey, "cursorPos.ch", String.valueOf(reqDTO.getCursorPos().getCh()));

        // ✅ 서버 현재 시간 기준으로 lastModified 처리
        OffsetDateTime lastModified = OffsetDateTime.now(ZoneOffset.UTC);
        redisRepository.updateHashValue(redisKey, "lastModified", lastModified.toString());

        // 3. 다른 사용자에게 브로드캐스트
        String roomId = reqDTO.getRoomId().toString();
        String diffText = reqDTO.getDiff().getText();
        webSocketBroadcaster.broadcastCodeUpdate(roomId, modifier, diffText);

        // 4. 응답 반환
        return new UpdateLiveCodingSnippetResDTO(
                modifier,
                reqDTO.getDiff(),
                lastModified,
                reqDTO.getCursorPos()
        );
    }

    private boolean isHost(LiveCodingDomain liveCodingDomain) {
        Long authenticatedUserNum = userUtil.getAuthenticatedUserNum();
        return authenticatedUserNum != null && authenticatedUserNum.equals(liveCodingDomain.getHostId());
    }

    private void saveLiveCodeSnippet(LiveCodingDomain liveCodingDomain) {
        String redisKey = LCD_CODE_PREFIX + liveCodingDomain.getHostId();

        Map<String, String> liveCodeSnippetMap = new HashMap<>();
        liveCodeSnippetMap.put("hostId", liveCodingDomain.getHostId().toString());
        liveCodeSnippetMap.put("roomId", liveCodingDomain.getRoomId().toString());
        liveCodeSnippetMap.put("code", LCD_CODE_INIT);
        liveCodeSnippetMap.put("language", "javascript");
        liveCodeSnippetMap.put("lastModified", Instant.now().toString());

        // diff 초기값 (0으로 초기화하면 가져와서 파싱할 때 안정적)
        liveCodeSnippetMap.put("diff.start", "0");
        liveCodeSnippetMap.put("diff.length", "0");
        liveCodeSnippetMap.put("diff.text", "");

        // cursorPos 초기값 (커서 위치 없음)
        liveCodeSnippetMap.put("cursorPos.line", "0");
        liveCodeSnippetMap.put("cursorPos.ch", "0");

        redisRepository.saveHash(redisKey, liveCodeSnippetMap);
        redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
    }

    private int parseSafeInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }



}
