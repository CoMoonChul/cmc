package com.sw.cmc.adapter.in.smtp.event;

import com.sw.cmc.application.port.in.smtp.SmtpUseCase;
import com.sw.cmc.domain.smtp.SendEmailHtmlDomain;
import com.sw.cmc.event.notice.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.sw.cmc.adapter.in.smtp.event
 * fileName       : SmtpEventListener
 * author         : ihw
 * date           : 2025. 2. 16.
 * description    : Smtp 이벤트 리스너
 */
@Component
@RequiredArgsConstructor
public class SmtpEventListener {
    private final SmtpUseCase smtpUseCase;
    @EventListener
    public void sendHtmlEmail(SendNotiEmailHtmlEvent sendNotiEmailHtmlEvent) throws Exception {
        SendEmailHtmlDomain sendEmailHtmlDomain = SendEmailHtmlDomain.builder()
                .title(sendNotiEmailHtmlEvent.getTitle())
                .message(sendNotiEmailHtmlEvent.getMessage())
                .link(sendNotiEmailHtmlEvent.getLink())
                .to(sendNotiEmailHtmlEvent.getTo())
                .subject(sendNotiEmailHtmlEvent.getSubject())
                .build();
        smtpUseCase.sendHtmlEmail(sendEmailHtmlDomain);
    }

    @EventListener
    public void sendEmailJoinEvent(SendEmailJoinEvent sendEmailJoinEvent) throws Exception {

        String title = "코문철 회원가입";
        String userName = sendEmailJoinEvent.getUserName();
        String message = userName + "님, 안녕하세요.\n\n 코문철 사이트 회원가입을 진심으로 축하드립니다.";

        SendEmailHtmlDomain sendEmailHtmlDomain = SendEmailHtmlDomain.builder()
                .title(title)
                .message(message)
                .link("")
                .to(sendEmailJoinEvent.getTo())
                .subject(title)
                .build();

        smtpUseCase.sendHtmlEmail(sendEmailHtmlDomain);
    }

    @EventListener
    public void sendEmailGroupInviteEvent(SendEmailGroupInviteEvent sendEmailGroupInviteEvent) throws Exception {

        String title = "코문철 멤버 초대장";
        String userName = sendEmailGroupInviteEvent.getUserName();
        String targetName = sendEmailGroupInviteEvent.getTargetName();
        String groupName = sendEmailGroupInviteEvent.getGroupName();

        String message =
                        targetName + "님, 안녕하세요.\n\n" +
                        userName + "님이 코문철 " + groupName + "그룹의 멤버로 초대했습니다.\n\n" +
                        "지금 바로 로그인을 진행해 주세요.";

        SendEmailHtmlDomain sendEmailHtmlDomain = SendEmailHtmlDomain.builder()
                .title(title)
                .message(message)
                .link("")
                .to(sendEmailGroupInviteEvent.getTo())
                .subject(title)
                .build();

        smtpUseCase.sendHtmlEmail(sendEmailHtmlDomain);
    }

    @EventListener
    public void SendEmailExitEvent(SendEmailExitEvent sendEmailExitEvent) throws Exception {

        String title = "회원 탈퇴";
        String message = "코문철 사이트에서 탈퇴 하셨습니다. 다음에 만나요!";

        SendEmailHtmlDomain sendEmailHtmlDomain = SendEmailHtmlDomain.builder()
                .title(title)
                .message(message)
                .link("")
                .to(sendEmailExitEvent.getTo())
                .subject(title)
                .build();

        smtpUseCase.sendHtmlEmail(sendEmailHtmlDomain);
    }

    @EventListener
    public void SendEmailAccountFindEvent(SendEmailAccountFindEvent sendEmailAccountFindEvent) throws Exception {

        String title = "코문철 계정 찾기";
        String userId = sendEmailAccountFindEvent.getUserId();
        String userName = sendEmailAccountFindEvent.getUserName();
        String passWord = sendEmailAccountFindEvent.getPassWord();

        String message =
                userName + "님, 안녕하세요.\n\n" +
                        userName + "님의 아이디와 새로운 비밀번호는 " + userId + " / " + passWord + " 입니다.\n\n" +
                        "지금 바로 로그인을 진행해 주세요.";


        SendEmailHtmlDomain sendEmailHtmlDomain = SendEmailHtmlDomain.builder()
                .title(title)
                .message(message)
                .link("")
                .to(sendEmailAccountFindEvent.getTo())
                .subject(title)
                .build();

        smtpUseCase.sendHtmlEmail(sendEmailHtmlDomain);
    }
}
