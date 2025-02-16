package com.sw.cmc.application.service.notice;

import com.sw.cmc.adapter.in.notice.dto.Notification;
import com.sw.cmc.adapter.out.notice.persistence.NoticeRepository;
import com.sw.cmc.application.port.in.notice.NoticeUseCase;
import com.sw.cmc.event.notice.SendNotiEmailEvent;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public List<Notification> selectNotice(String userNum) throws Exception {
        List<Notification> notice = noticeRepository.findAllByUserNum(userNum);
        return notice;
    }

    public void ExampleFunction() throws Exception {
        // event.notice.SendNotiEmailEvent는 adapter.in.smtp.event.SmtpEventListener를 트리거하게 됩니다.
        // (SendNotiEmailEvent라는 타입 기반 이벤트 트리거)
        // SendNotiEmailEvent에는
        // 이메일 발송에 필요한, 알림 쪽에서 세팅해 줄 수 있는 정보들을 만드시면 되고
        // 만드신 정보를 수신한 adapter.in.smtp.event.SmtpEventListener.handleSendNotiEmail 함수에서
        // 이메일 수신용 도메인으로 정제하여 메일을 발송시킵니다.
        // 편의상 SendNotiEmailEvent와 SendEmailDomain의 멤버 변수를 같게 했으나,
        // smtp 도메인에서 처리해야 하는 내용이라고 판단되면 SendNotiEmailEvent에는 정제 전 데이터가 포함되면 됩니다.
        String userName = "임현우";
        String template = "안녕!";
        String email = "fddsgt1234@naver.com";
        String type = "01";

        SendNotiEmailEvent sendNotiEmailEvent = SendNotiEmailEvent.builder()
                .rsvrEmail(email)
                .subject(template + userName)
                .text(template + (StringUtils.equals(type, "01") ? "반가워" : "오랜만이야"))
                .build();
        eventPublisher.publishEvent(sendNotiEmailEvent);
    }
}
