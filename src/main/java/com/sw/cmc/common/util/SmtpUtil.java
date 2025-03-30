package com.sw.cmc.common.util;

import com.sw.cmc.event.notice.SendEmailAccountFindEvent;
import com.sw.cmc.event.notice.SendEmailExitEvent;
import com.sw.cmc.event.notice.SendEmailGroupInviteEvent;
import com.sw.cmc.event.notice.SendEmailJoinEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.sw.cmc.common.util
 * fileName       : SmtpUtil
 * author         : An Seung Gi
 * date           : 2025-03-27
 * description    : 이메일 전송 공통 유틸
 */

@Component
@RequiredArgsConstructor
public class SmtpUtil {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 회원 가입 이메일 발송
     * @param email
     * @param username
     * @param userId
     * @param password
     * @throws Exception
     */
    public void sendEmailJoin(String email, String username, String userId, String password) throws Exception {
        SendEmailJoinEvent sendEmailJoinEvent = SendEmailJoinEvent.builder()
                .to(email)
                .userName(username)
                .userId(userId)
                .passWord(password)
                .build();

        eventPublisher.publishEvent(sendEmailJoinEvent);
    }

    /**
     * 그룹 초대 이메일 발송
     * @param email
     * @param username
     * @param groupName
     * @param targetName
     * @throws Exception
     */
    public void sendEmailGroupInvite(String email, String username, String groupName, String targetName) throws Exception {
        SendEmailGroupInviteEvent sendEmailGroupInviteEvent = SendEmailGroupInviteEvent.builder()
                .to(email)
                .targetName(targetName)
                .userName(username)
                .groupName(groupName)
                .build();

        eventPublisher.publishEvent(sendEmailGroupInviteEvent);
    }


    /**
     * 회원 탈퇴 이메일 전송
     * @param email
     * @param username
     * @throws Exception
     */
    public void sendEmailExit(String email, String username) throws Exception {
        SendEmailExitEvent sendEmailExitEvent = SendEmailExitEvent.builder()
                .to(email)
                .userName(username)
                .build();

        eventPublisher.publishEvent(sendEmailExitEvent);
    }

    public void sendEmailFindAccount(String email, String username, String userId, String password) throws Exception {
        SendEmailAccountFindEvent sendEmailAccountFindEvent = SendEmailAccountFindEvent.builder()
                .to(email)
                .userName(username)
                .userId(userId)
                .passWord(password)
                .build();

        eventPublisher.publishEvent(sendEmailAccountFindEvent);
    }



}
