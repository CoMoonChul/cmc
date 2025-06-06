package com.sw.cmc.application.service.lcd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.cmc.adapter.in.lcd.web.WebSocketBroadcaster;
import com.sw.cmc.adapter.in.lcd.web.WebSocketRoomManager;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

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
    private final WebSocketRoomManager webSocketRoomManager;
    private final ObjectMapper objectMapper;
    private static final String LCD_PREFIX = LiveCodingConstants.LCD_PREFIX;
    private static final String LCD_CODE_PREFIX = LiveCodingConstants.LCD_CODE_PREFIX;

    @Value("${domain}")
    private String inviteDomain;

    @Override
    public LiveCodingDomain createLiveCoding(Long hostId) throws CmcException {

        Long currentUser = userUtil.getAuthenticatedUserNum();
        if (!Objects.equals(currentUser, hostId)) {
            throw new CmcException("LCD008");
        }

        // 기존 방이 있으면 삭제
        if (this.existsByHostId(hostId)) {
            // 기존 roomId를 추출
            Map<String, String> existingCodeMap = redisRepository.selectHash(LCD_CODE_PREFIX + hostId);
            if (existingCodeMap != null && existingCodeMap.containsKey("roomId")) {
                UUID existingRoomId = UUID.fromString(existingCodeMap.get("roomId"));

                // 세션 종료 처리
                String roomIdStr = existingRoomId.toString();
                Set<WebSocketSession> existingSessions = webSocketRoomManager.getSessions(roomIdStr);
                for (WebSocketSession session : existingSessions) {
                    try {
                        if (session.isOpen()) {
                            session.close(CloseStatus.NORMAL);
                        }
                    } catch (Exception e) {
                        throw new CmcException("LCD022");
                    }
                }

                webSocketRoomManager.removeRoom(roomIdStr);

                // Redis 상의 방 정보 제거
                this.deleteLiveCoding(existingRoomId);
            }
        }


        UUID roomId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        List<Long> participants = new ArrayList<>();
        participants.add(hostId);
        Integer participantCount = 1;
        String link = this.generateInviteLink(roomId);
        System.out.println("createLiveCoding link: " + link);

        LiveCodingDomain liveCodingDomain = new LiveCodingDomain(
                roomId,  // 생성된 방 ID
                hostId,  // 방장 ID
                createdAt,  // 방 생성 시간
                participantCount,  // 참가자 수
                participants,  // 참가자 목록
                link   // 링크
        );

        this.saveLiveCoding(liveCodingDomain);
        this.saveLiveCodeSnippet(liveCodingDomain);

        return liveCodingDomain;
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
        return inviteDomain + "/livecoding/join?token=" + jwtToken.getAccessToken();
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
    public boolean existsByHostId(Long hostId) {
        return redisRepository.selectHash(LCD_CODE_PREFIX + hostId) != null;
    }

    @Override
    public LiveCodingDomain findByRoomId(UUID roomId) {
        String key = LCD_PREFIX + roomId;
        Map<String, String> liveCodingMap = redisRepository.selectHash(key);

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

        List<LiveCodeSnippetDomain.Diff> diffList;
        try {
            diffList = objectMapper.readValue(
                    liveCodeMap.getOrDefault("diff", "[]"),
                    new TypeReference<List<LiveCodeSnippetDomain.Diff>>() {}

            );
        } catch (JsonProcessingException e) {
            throw new CmcException("LCD020");
        }

        return new LiveCodeSnippetDomain(
                UUID.fromString(liveCodeMap.get("roomId")),
                liveCodeMap.get("code"),
                diffList,
                liveCodeMap.get("language"),
                lastModified,
                new LiveCodeSnippetDomain.CursorPosition(line, ch),
                hostId
        );
    }

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

        String diffJson;
        try {
            diffJson = objectMapper.writeValueAsString(reqDTO.getDiff());
        } catch (JsonProcessingException e) {
            throw new CmcException("LCD021"); // serialize 실패
        }
        redisRepository.updateHashValue(redisKey, "diff", diffJson);
        redisRepository.updateHashValue(redisKey, "code", String.valueOf(reqDTO.getCode()));
        redisRepository.updateHashValue(redisKey, "cursorPos.line", String.valueOf(reqDTO.getCursorPos().getLine()));
        redisRepository.updateHashValue(redisKey, "cursorPos.ch", String.valueOf(reqDTO.getCursorPos().getCh()));
        redisRepository.updateHashValue(redisKey, "language", reqDTO.getLanguage());
        OffsetDateTime lastModified = OffsetDateTime.now(ZoneOffset.UTC);
        redisRepository.updateHashValue(redisKey, "lastModified", lastModified.toString());

        // 3. 다른 사용자에게 브로드캐스트
        if (Boolean.TRUE.equals(reqDTO.getIsBroadcast())) {
            String roomId = reqDTO.getRoomId().toString();
            webSocketBroadcaster.broadcastCodeUpdate(roomId, modifier, diffJson, reqDTO.getCursorPos(), reqDTO.getLanguage());
        }

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
        liveCodeSnippetMap.put("code", ""); // 초기 코드는 빈 문자열
        liveCodeSnippetMap.put("language", "javascript"); // 언어는 클라이언트가 나중에 설정

        // 초기 diff: 빈 리스트 → 직렬화
        String emptyDiffJson;
        try {
            emptyDiffJson = objectMapper.writeValueAsString(Collections.emptyList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize empty diff list", e);
        }
        liveCodeSnippetMap.put("diff", emptyDiffJson);

        // 초기 커서 위치: (0, 0)
        liveCodeSnippetMap.put("cursorPos.line", "0");
        liveCodeSnippetMap.put("cursorPos.ch", "0");

        // 마지막 수정 시간
        liveCodeSnippetMap.put("lastModified", Instant.now().toString());

        // 저장 및 만료 시간 설정
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
