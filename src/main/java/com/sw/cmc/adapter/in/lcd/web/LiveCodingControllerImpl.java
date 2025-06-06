package com.sw.cmc.adapter.in.lcd.web;

import com.sw.cmc.adapter.in.livecoding.dto.*;
import com.sw.cmc.adapter.in.livecoding.web.LiveCodingControllerApi;
import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.domain.lcd.LiveCodeSnippetDomain;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * packageName    : com.sw.cmc.adapter.in.web
 * fileName       : LiveCodingController
 * author         : Ko
 * date           : 2025-02-08
 * description    : 실시간 코딩 controller
 */
@RestController
@RequiredArgsConstructor
public class LiveCodingControllerImpl implements LiveCodingControllerApi {
    private final LiveCodingUseCase liveCodingUseCase;


    @Override
    public ResponseEntity<CreateLiveCodingResDTO> createLiveCoding(CreateLiveCodingReqDTO createLiveCodingReqDTO) throws Exception {
        LiveCodingDomain liveCodingDomain = liveCodingUseCase.createLiveCoding(createLiveCodingReqDTO.getHostId());
        CreateLiveCodingResDTO response = new CreateLiveCodingResDTO();
        response.setRoomId(liveCodingDomain.getRoomId());
        return ResponseEntity.ok(response);  // 200 OK 응답 반환
    }

    @Override
    public ResponseEntity<DeleteLiveCodingResDTO> deleteLiveCoding(DeleteLiveCodingReqDTO deleteLiveCodingReqDTO) throws Exception {
        // 유스케이스에서 방 삭제 처리
        boolean deleted = liveCodingUseCase.deleteLiveCoding(deleteLiveCodingReqDTO.getRoomId());

        // 응답 DTO 생성
        DeleteLiveCodingResDTO response = new DeleteLiveCodingResDTO();


        if (deleted) {
            response.setStatus("SUCCESS");
            return ResponseEntity.ok(response);
        } else {
            throw new CmcException("LCD004");
        }

    }

    @Override
    public ResponseEntity<InviteLiveCodingResDTO> inviteLiveCoding(@RequestBody InviteLiveCodingReqDTO inviteLiveCodingReqDTO) throws Exception {
        // roomId를 받음
        UUID roomId = inviteLiveCodingReqDTO.getRoomId();

        // 초대 링크 생성 서비스 호출
        String inviteLink = liveCodingUseCase.generateInviteLink(roomId);

        // 응답 DTO 생성
        InviteLiveCodingResDTO response = new InviteLiveCodingResDTO();
        response.setInviteLink(inviteLink);

        return ResponseEntity.ok(response);  // 200 OK 응답 반환
    }

    @Override
    public ResponseEntity<SelectLiveCodingResDTO> selectLiveCoding(UUID roomId) throws Exception {
        // roomId를 이용하여 라이브 코딩 방 정보 조회
        LiveCodingDomain liveCodingDomain = liveCodingUseCase.selectLiveCoding(roomId);

        // 조회된 정보를 기반으로 응답 DTO 생성
        SelectLiveCodingResDTO response = new SelectLiveCodingResDTO();
        response.setRoomId(liveCodingDomain.getRoomId());
        response.setHostId(liveCodingDomain.getHostId());
        response.setCreatedAt(String.valueOf(liveCodingDomain.getCreatedAt()));
        response.setParticipantCount(liveCodingDomain.getParticipantCount());
        response.setParticipants(liveCodingDomain.getParticipants());
        response.setLink(liveCodingDomain.getLink());

        // 응답 반환
        return ResponseEntity.ok(response);  // 200 OK 응답 반환
    }

    @Override
    public ResponseEntity<UpdateLiveCodingResDTO> updateLiveCoding(UpdateLiveCodingReqDTO updateLiveCodingReqDTO) throws Exception {
        // 라이브 코딩 방 업데이트 실행
        LiveCodingDomain liveCodingDomain = liveCodingUseCase.updateLiveCoding(
                updateLiveCodingReqDTO.getRoomId(),
                updateLiveCodingReqDTO.getUserNum(),
                updateLiveCodingReqDTO.getAction()
        );

        // 응답 DTO 생성
        UpdateLiveCodingResDTO response = new UpdateLiveCodingResDTO();
        response.setRoomId(liveCodingDomain.getRoomId());
        response.setParticipantCount(liveCodingDomain.getParticipantCount());

        return ResponseEntity.ok(response);  // 200 OK 응답 반환
    }

    @Override
    public ResponseEntity<ExistLiveCodingResDTO> existLiveCoding(Long hostId)  {
        // 라이브 코딩 방 존재 여부 확인
        boolean exists = liveCodingUseCase.existsByHostId(hostId);

        // 응답 DTO 생성
        ExistLiveCodingResDTO response = new ExistLiveCodingResDTO();
        response.setExists(exists);

        return ResponseEntity.ok(response);  // 200 OK 응답 반환
    }

    @Override
    public ResponseEntity<VerifyLiveCodingResDTO> verifyLiveCoding(VerifyLiveCodingReqDTO verifyLiveCodingReqDTO) throws Exception {
        LiveCodingDomain verifiedRoom = liveCodingUseCase.verifyLiveCoding(
                 verifyLiveCodingReqDTO.getToken()
        );

        VerifyLiveCodingResDTO response = new VerifyLiveCodingResDTO();
        response.setRoomId(verifiedRoom.getRoomId());

        return ResponseEntity.ok(response);  // 200 OK 응답 반환

    }

    @Override
    public ResponseEntity<SelectLiveCodingSnippetResDTO> selectLiveCodingSnippet(Long hostId) throws Exception {
        LiveCodeSnippetDomain LiveCodeSnippetDomain = liveCodingUseCase.selectLiveCodingSnippet(hostId);
        SelectLiveCodingSnippetResDTO response = new SelectLiveCodingSnippetResDTO();
        if (LiveCodeSnippetDomain == null) {
            response.setLanguage("javscript");
            response.setLivecode("");
            return ResponseEntity.ok(response);
        }
        response.setLanguage(LiveCodeSnippetDomain.getLanguage());
        response.setLivecode(LiveCodeSnippetDomain.getCode());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UpdateLiveCodingSnippetResDTO> updateLiveCodingSnippet(UpdateLiveCodingSnippetReqDTO reqDTO) throws Exception {
        UpdateLiveCodingSnippetResDTO resDTO = liveCodingUseCase.updateLiveCodingSnippet(reqDTO);
        return ResponseEntity.ok(resDTO);
    }

}



