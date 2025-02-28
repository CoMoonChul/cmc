package com.sw.cmc.adapter.in.lcd.web;

import com.sw.cmc.adapter.in.livecoding.dto.CreateLiveCodingReqDTO;
import com.sw.cmc.adapter.in.livecoding.dto.CreateLiveCodingResDTO;
import com.sw.cmc.adapter.in.livecoding.dto.DeleteLiveCodingReqDTO;
import com.sw.cmc.adapter.in.livecoding.dto.DeleteLiveCodingResDTO;
import com.sw.cmc.adapter.in.livecoding.web.LiveCodingControllerApi;
import com.sw.cmc.application.port.in.lcd.DeleteLcdCase;
import com.sw.cmc.application.port.in.lcd.LiveCodingUseCase;
import com.sw.cmc.domain.lcd.LiveCodingDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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

        return switch (deletionStatus) {
            case SUCCESS -> {
                response.setStatus("SUCCESS");
                yield ResponseEntity.ok(response);
            }
//            case NOT_FOUND:
//                response.setStatus("나중에");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // 404 응답
            default -> {
                response.setStatus("FAIL");
                yield ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  // 500 응답
            }
        };
    }
}



