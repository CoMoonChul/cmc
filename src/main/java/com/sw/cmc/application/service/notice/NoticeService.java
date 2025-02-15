package com.sw.cmc.application.service.notice;

import com.sw.cmc.adapter.in.notice.dto.Notification;
import com.sw.cmc.adapter.out.notice.persistence.NoticeRepository;
import com.sw.cmc.application.port.in.notice.NoticeUseCase;
import com.sw.cmc.domain.notice.NoticeDomain;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.sw.cmc.application.service.notice
 * fileName       : NoticeService
 * author         : An Seung Gi
 * date           : 2025-02-15
 * description    :
 */
@Service
@RequiredArgsConstructor
public class NoticeService implements NoticeUseCase {

    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final NoticeRepository noticeRepository;


    @Override
    public List<Notification> selectNotice(String userNum) throws Exception {
        List<Notification> notice = noticeRepository.findAllByUserNum(userNum);
        return notice;
    }
}
