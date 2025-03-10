package com.sw.cmc.adapter.in.group.web;

import com.sw.cmc.adapter.in.group.dto.*;
import com.sw.cmc.application.port.in.group.GroupUseCase;
import com.sw.cmc.domain.group.GroupDomain;
import com.sw.cmc.entity.GroupMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * packageName    : com.sw.cmc.adapter.in.group.web
 * fileName       : GroupControllerImpl
 * author         : SungSuHan
 * date           : 2025-03-03
 * description    : group-controller
 */
@RestController
@RequiredArgsConstructor
public class GroupControllerImpl implements GroupControllerApi {

    private final GroupUseCase groupUseCase;

    @Override
    public ResponseEntity<CreateResDTO> create(CreateReqDTO createReqDTO) throws Exception {

        GroupDomain groupDomain = GroupDomain.builder()
                .groupName(createReqDTO.getGroupName())
                .build();

        return ResponseEntity.ok(new CreateResDTO().resultMessage(groupUseCase.create(groupDomain)));
    }

    @Override
    public ResponseEntity<GetGroupMemberListResDTO> getGroupMemberList(Long groupId) throws Exception {

        GroupDomain groupDomain = GroupDomain.builder()
                .groupId(groupId)
                .build();

        List<GroupMember> memberList = groupUseCase.getGroupMemberList(groupDomain).getMembers();

        List<GetGroupMemberListResDTOMembersInner> members = memberList.stream()
                .map(member -> {
                    GetGroupMemberListResDTOMembersInner dto = new GetGroupMemberListResDTOMembersInner();
                    dto.setUserNum(member.getUser().getUserNum());
                    dto.setUsername(member.getUser().getUsername());
                    dto.setEmail(member.getUser().getEmail());
                    dto.setGroupRole(member.getGroupRole());
                    dto.setGroupMemberId(member.getGroupMemberId());
                    return dto;
                })
                .toList();

        GetGroupMemberListResDTO response = new GetGroupMemberListResDTO();
        response.setMembers(members);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<DeleteResDTO> delete(DeleteReqDTO deleteReqDTO) throws Exception {

        GroupDomain groupDomain = GroupDomain.builder()
                .groupId(deleteReqDTO.getGroupId())
                .build();

        return ResponseEntity.ok(new DeleteResDTO().resultMessage(groupUseCase.delete(groupDomain)));
    }

    @Override
    public ResponseEntity<InviteResDTO> invite(InviteReqDTO inviteReqDTO) throws Exception {

        GroupDomain groupDomain = GroupDomain.builder()
                .username(inviteReqDTO.getUsername())
                .groupId(inviteReqDTO.getGroupId())
                .build();

        return ResponseEntity.ok(new InviteResDTO().resultMessage(groupUseCase.invite(groupDomain)));
    }

    @Override
    public ResponseEntity<ExpelResDTO> expel(ExpelReqDTO expelReqDTO) throws Exception {

        GroupDomain groupDomain = GroupDomain.builder()
                .userNum(expelReqDTO.getUserNum())
                .groupId(expelReqDTO.getGroupId())
                .build();

        return ResponseEntity.ok(new ExpelResDTO().resultMessage(groupUseCase.expel(groupDomain)));
    }

}
