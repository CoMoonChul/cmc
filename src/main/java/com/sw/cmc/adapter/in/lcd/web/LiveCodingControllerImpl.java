package com.sw.cmc.adapter.in.lcd.web;

import com.sw.cmc.adapter.in.livecoding.dto.*;
import com.sw.cmc.adapter.in.livecoding.web.LiveCodingControllerApi;
import com.sw.cmc.application.port.in.lcd.DeleteLcdCase;
import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.common.advice.CmcException;
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
        DeleteLcdCase deletionStatus = liveCodingUseCase.deleteLiveCoding(deleteLiveCodingReqDTO.getRoomId());

        // 응답 DTO 생성
        DeleteLiveCodingResDTO response = new DeleteLiveCodingResDTO();


        if (deletionStatus == DeleteLcdCase.SUCCESS) {
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
                updateLiveCodingReqDTO.getAction().name()
        );

        // 응답 DTO 생성
        UpdateLiveCodingResDTO response = new UpdateLiveCodingResDTO();
        response.setRoomId(liveCodingDomain.getRoomId());
        response.setParticipantCount(liveCodingDomain.getParticipantCount());

        return ResponseEntity.ok(response);  // 200 OK 응답 반환
    }


}



