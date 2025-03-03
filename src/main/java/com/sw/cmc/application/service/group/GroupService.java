package com.sw.cmc.application.service.group;

import com.sw.cmc.adapter.out.group.persistence.GroupMemberRepository;
import com.sw.cmc.adapter.out.group.persistence.GroupRepository;
import com.sw.cmc.application.port.in.group.GroupUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.group.GroupDomain;
import com.sw.cmc.entity.Group;
import com.sw.cmc.entity.GroupMember;
import com.sw.cmc.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final ModelMapper modelMapper;
    private final GroupRepository groupRepository;
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
        User user = new User();
        user.setUserNum(userUtil.getAuthenticatedUserNum());

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
        System.out.println("@@@@@@@@@@@@@@@@@@@");
        final List<GroupMember> groupMembers = groupMemberRepository.findByGroup_GroupId(groupDomain.getGroupId());

        return GroupDomain.builder()
                .members(groupMembers)
                .build();
    }
}
