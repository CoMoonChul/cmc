package com.sw.cmc.adapter.in.notice.web;

import com.sw.cmc.adapter.in.notice.dto.SelectNoticeListDTO;
import com.sw.cmc.adapter.in.notice.dto.SelectNoticeResDTO;
import com.sw.cmc.application.port.in.notice.NoticeUseCase;
import com.sw.cmc.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * packageName    : com.sw.cmc.adapter.in.notice
 * fileName       : NoticeControllerImpl
 * author         : An Seung Gi
 * date           : 2025-02-15
 * description    :
 */
@RestController
@RequiredArgsConstructor
public class NoticeControllerImpl implements NoticeControllerApi {

    private final ModelMapper modelMapper;
    private final NoticeUseCase noticeUseCase;
    @Override
    public ResponseEntity<SelectNoticeListDTO> selectNotice(String userNum) throws Exception {
        // 1. UseCase에서 데이터 조회 (List<Notification> 반환)
        List<Notification> result = noticeUseCase.selectNotice(userNum);

        System.out.println("noticeUseCase Result: " + result);

        // 2. 리스트 매핑 (stream()을 사용해 안전하게 변환)
        List<SelectNoticeResDTO> noticeResDTOList = result.stream()
                .map(notification -> modelMapper.map(notification, SelectNoticeResDTO.class))
                .toList();

        System.out.println("noticeResDTOList DTO: " + noticeResDTOList);

        // 3. 최종 DTO 생성
        SelectNoticeListDTO responseDto = new SelectNoticeListDTO();
        responseDto.setTotal(noticeResDTOList.size()); // 총 개수 설정
        responseDto.setNoticeList(noticeResDTOList); // 리스트 설정

        System.out.println("Mapped Response DTO: " + responseDto);

        return ResponseEntity.ok(responseDto);
    }


}
