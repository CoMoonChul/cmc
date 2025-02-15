package com.sw.cmc.adapter.in.notice;

import com.sw.cmc.adapter.in.notice.dto.SelectNoticeResDTO;
import com.sw.cmc.adapter.in.notice.web.NoticeControllerApi;
import com.sw.cmc.application.port.in.notice.NoticeUseCase;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<SelectNoticeResDTO> selectNotice(String userNum) throws Exception {
        return ResponseEntity.ok(modelMapper.map(noticeUseCase.selectNotice(userNum), SelectNoticeResDTO.class));
    }

}
