package com.sw.cmc.application.service.group;

import com.sw.cmc.adapter.out.group.persistence.GroupMemberRepository;
import com.sw.cmc.adapter.out.group.persistence.GroupRepository;
import com.sw.cmc.adapter.out.user.persistence.UserRepository;
import com.sw.cmc.application.port.in.group.GroupUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.group.GroupDomain;
import com.sw.cmc.entity.Group;
import com.sw.cmc.entity.GroupMember;
import com.sw.cmc.entity.User;
import com.sw.cmc.event.notice.SendEmailGroupInviteEvent;
import com.sw.cmc.event.notice.SendNotiInAppEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.sw.cmc.domain.group.GroupDomain.validateGroupName;

/**
 * packageName    : com.sw.cmc.application.service.group
 * fileName       : GroupService
 * author         : SungSuHan
 * date           : 2025-03-03
 * description    :
 */
@Service
@RequiredArgsConstructor
public class GroupService implements GroupUseCase {

    private final UserUtil userUtil;
    private final MessageUtil messageUtil;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final GroupMemberRepository groupMemberRepository;

    @Override
    @Transactional
    public String create(GroupDomain groupDomain) throws Exception {
        // 유효성 검사
        validateGroupName(groupDomain.getGroupName());

        // 그룹명 중복 검사
        if (groupRepository.existsByGroupName(groupDomain.getGroupName())) {
            throw new CmcException("USER022");
        }

        // 회원 세팅
        final User user = userRepository.findByUserNum(userUtil.getAuthenticatedUserNum())
                .orElseThrow(() -> new CmcException("USER001"));

        // 그룹 세팅
        Group group = new Group();
        group.setGroupName(groupDomain.getGroupName());

        // 그룹 멤버 세팅
        GroupMember groupMember = new GroupMember();
        groupMember.setUser(user);
        groupMember.setGroupRole("MASTER");

        group.addGroupMember(groupMember);

        // 그룹 생성
        groupRepository.save(group);

        return messageUtil.getFormattedMessage("USER021");
    }

    @Override
    public GroupDomain getGroupMemberList(GroupDomain groupDomain) throws Exception {
        final List<GroupMember> groupMembers = groupMemberRepository.findByGroupIdWithUser(groupDomain.getGroupId());

        return GroupDomain.builder()
                .members(groupMembers)
                .build();
    }

    @Override
    @Transactional
    public String delete(GroupDomain groupDomain) throws Exception {
        final GroupMember groupMaster = groupMemberRepository.findByGroup_GroupIdAndUser_UserNum(groupDomain.getGroupId(), userUtil.getAuthenticatedUserNum())
                .orElseThrow(() -> new CmcException("USER023"));

        // 마스터 권한 확인
        if (!groupMaster.getGroupRole().equals("MASTER")) {
            throw new CmcException("USER028");
        }

        groupRepository.deleteByGroupId(groupDomain.getGroupId());

        return messageUtil.getFormattedMessage("USER024");
    }

    @Override
    @Transactional
    public String invite(GroupDomain groupDomain) throws Exception {
        final User invitee = userRepository.findByUsername(groupDomain.getUsername())
                .orElseThrow(() -> new CmcException("USER001"));

        // 유효성 검사
        if (groupMemberRepository.findByGroup_GroupId(groupDomain.getGroupId()).size() > 4) {
            throw new CmcException("USER026");
        }

        final Group group = groupRepository.findByGroupId(groupDomain.getGroupId())
                .orElseThrow(() -> new CmcException("USER023"));

        // 그룹 멤버 세팅
        GroupMember groupMember = new GroupMember();
        groupMember.setUser(invitee);
        groupMember.setGroupRole("SLAVE");

        group.addGroupMember(groupMember);

        // 그룹 멤버 생성
        groupRepository.save(group);

        final User inviter = userRepository.findByUserNum(userUtil.getAuthenticatedUserNum())
                .orElseThrow(() -> new CmcException("USER001"));

        // 이메일 전송
        sendEmail(invitee.getEmail(), inviter.getUsername(), group.getGroupName(), invitee.getUsername());
        // 인앱 알림
        sendNotice("groupNm", group.getGroupName());

        return messageUtil.getFormattedMessage("USER025");
    }

    @Override
    public String expel(GroupDomain groupDomain) throws Exception {
        final GroupMember groupMember = groupMemberRepository.findByGroup_GroupIdAndUser_UserNum(groupDomain.getGroupId(), groupDomain.getUserNum())
                .orElseThrow(() -> new CmcException("USER023"));

        final GroupMember groupMaster = groupMemberRepository.findByGroup_GroupIdAndUser_UserNum(groupDomain.getGroupId(), userUtil.getAuthenticatedUserNum())
                .orElseThrow(() -> new CmcException("USER023"));

        // 마스터 권한 확인
        if (!groupMaster.getGroupRole().equals("MASTER")) {
            throw new CmcException("USER028");
        }

        // 멤버 삭제
        groupMemberRepository.delete(groupMember);

        return messageUtil.getFormattedMessage("USER027");
    }

    public void sendEmail(String email, String username, String groupName, String targetName) throws Exception {
        SendEmailGroupInviteEvent sendEmailGroupInviteEvent = SendEmailGroupInviteEvent.builder()
                .to(email)
                .targetName(targetName)
                .userName(username)
                .groupName(groupName)
                .build();

        eventPublisher.publishEvent(sendEmailGroupInviteEvent);


    }

    public void sendNotice(String paramKey, String paramVal) {
        // 인앱 알림 (notice 테이블에 저장)
        Long userNum = userUtil.getAuthenticatedUserNum();
        Map<String, String> templateParams = Map.of(
                paramKey, paramVal
        ); // 템플릿 내용

        SendNotiInAppEvent sendNotiInAppEvent = SendNotiInAppEvent.builder()
                .notiTemplateId(3L)
                .sendAt(LocalDateTime.now().toString())
                .linkUrl("")
                .createUser(userNum)
                .sendState("Y")
                .templateParams(templateParams)
                .build();

        eventPublisher.publishEvent(sendNotiInAppEvent);
    }
}
