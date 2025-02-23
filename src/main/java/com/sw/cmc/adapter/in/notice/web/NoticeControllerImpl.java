package com.sw.cmc.adapter.in.notice.web;
import com.sw.cmc.adapter.in.notice.dto.DeleteNoticeReqDTO;
import com.sw.cmc.adapter.in.notice.dto.DeleteNoticeResDTO;
import com.sw.cmc.adapter.in.notice.dto.SelectNoticeListDTO;
import com.sw.cmc.adapter.in.notice.dto.SelectNoticeResDTO;
import com.sw.cmc.application.port.in.notice.NoticeUseCase;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.notice.NotiListDomain;
import com.sw.cmc.domain.notice.NoticeDomain;
import com.sw.cmc.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.sw.cmc.adapter.in.notice
 * fileName       : NoticeControllerImpl
 * author         : An Seung Gi
 * date           : 2025-02-15
 * description    : notice controller
 */
@RestController
@RequiredArgsConstructor
public class NoticeControllerImpl implements NoticeControllerApi {

    private final ModelMapper modelMapper;
    private final NoticeUseCase noticeUseCase;

    @Override
    public ResponseEntity<SelectNoticeListDTO> selectPageNotice(Integer page, Integer size) throws Exception {
        NotiListDomain result = noticeUseCase.selectPageNotice(page, size);
        return ResponseEntity.ok(modelMapper.map(result, SelectNoticeListDTO.class));
    }

    @Override
    public ResponseEntity<DeleteNoticeResDTO> deleteNotice(DeleteNoticeReqDTO deleteNoticeReqDTO) throws Exception {
        NoticeDomain noticeDomain = NoticeDomain.builder()
                .notiId(deleteNoticeReqDTO.getNotiId())
                .build();
        return ResponseEntity.ok(modelMapper.map(noticeUseCase.deleteNotice(noticeDomain), DeleteNoticeResDTO.class));
    }
}
