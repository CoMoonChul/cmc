package com.sw.cmc.application.service.notice;

import com.sw.cmc.adapter.out.notice.persistence.NoticeRepository;
import com.sw.cmc.adapter.out.notice.persistence.NoticeTemplateRepository;
import com.sw.cmc.application.port.in.notice.NoticeUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.notice.NotiListDomain;
import com.sw.cmc.domain.notice.NoticeDomain;
import com.sw.cmc.entity.Notification;
import com.sw.cmc.entity.NotificationTemplate;
import com.sw.cmc.event.notice.SendNotiEmailEvent;
import com.sw.cmc.event.notice.SendNotiEmailHtmlEvent;
import com.sw.cmc.event.notice.SendNotiInAppEvent;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * packageName    : com.sw.cmc.application.service.notice
 * fileName       : NoticeService
 * author         : An Seung Gi
 * date           : 2025-02-15
 * description    : notice service
 */
@Service
@RequiredArgsConstructor
public class NoticeService implements NoticeUseCase {

    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final NoticeRepository noticeRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserUtil userUtil;
    private final NoticeTemplateRepository noticeTemplateRepository;


    @Override
    public NotiListDomain selectPageNotice(Integer page, Integer size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Long userNum = userUtil.getAuthenticatedUserNum();
        Page<Notification> res = noticeRepository.findByUserNum(userNum, pageable);
        List<NoticeDomain> list = res.getContent().stream()
                .map(n -> modelMapper.map(n, NoticeDomain.class)) // 람다식으로 개별 매핑
                .toList();

        // 이벤트 리스너 테스트
        Long notiTemplateId = 3L; // 템플릿 ID
        String sendAt = "20250225"; // 발송시간
        String linkUrl = "/testJoinUrl"; // 알림 url
        Long createUser = userNum; // 알림 생성자
        String sendState = "Y"; // 발송상태
        Map<String, String> templateParams = Map.of(
            "groupNm", "코문철파티"
        ); // 템플릿 내용

        eventPublisher.publishEvent(new SendNotiInAppEvent(userNum, notiTemplateId, sendAt, linkUrl, createUser, sendState, templateParams));

        String title = "이메일 타이틀 html";
        String message = "이메일 내용 html";
        String link = "https://www.naver.com";
        String to = "sgan@softworks.co.kr";
        String subject = "이메일 제목 html";

        eventPublisher.publishEvent(new SendNotiEmailHtmlEvent(to, subject, title, message, link));


        return NotiListDomain.builder()
                .pageNumber(res.getPageable().getPageNumber())
                .pageSize(res.getPageable().getPageSize())
                .totalPages(res.getTotalPages())
                .totalElements(res.getTotalElements())
                .notiList(list)
                .build();
    }

    @Override
    @Transactional
    public NoticeDomain saveNotification(NoticeDomain noticeDomain) throws Exception {
        // 1. NotificationTemplate 조회 (DB에서 가져오기)
        NotificationTemplate template = noticeTemplateRepository.findById(noticeDomain.getNotiTemplateId())
                .orElseThrow(() -> new CmcException("NOTI001"));
        String notiTemplate = template.getNotiContent();
        String reasonNoti = replacePlaceholders(notiTemplate, noticeDomain.getTemplateParams());

        NoticeDomain notiResult = NoticeDomain.builder()
                .userNum(noticeDomain.getUserNum())
                .sendAt(noticeDomain.getSendAt())
                .linkUrl(noticeDomain.getLinkUrl())
                .notiTemplate(template)
                .createUser(noticeDomain.getCreateUser())
                .sendState(noticeDomain.getSendState())
                .reasonNoti(reasonNoti)
                .build();


        Notification notification = modelMapper.map(notiResult, Notification.class);

        Notification saved = noticeRepository.save(notification);
        entityManager.refresh(saved);

        return notiResult;
    }


    @Override
    @Transactional
    public NoticeDomain deleteNotice(NoticeDomain noticeDomain) throws Exception {
        noticeRepository.deleteById(noticeDomain.getNotiId());
        return noticeDomain;
    }

    @Override
    @Transactional
    public int deleteAllNotice() throws Exception {
        Long userNum = userUtil.getAuthenticatedUserNum();
        return noticeRepository.deleteByUserNum(userNum);
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

    private String replacePlaceholders(String template, Map<String, String> values) {
        // 정규 표현식을 사용하여 {}로 감싸진 부분을 찾음
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(template);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String placeholder = matcher.group(1); // {} 내부의 텍스트를 추출
            String replacement = values.get(placeholder); // 대체할 값 찾기
            if (replacement == null) {
                ///
            }
            matcher.appendReplacement(result, com.github.jknack.handlebars.internal.lang3.StringUtils.defaultString(String.valueOf(replacement), placeholder));
        }
        matcher.appendTail(result);

        return result.toString();
    }




}
