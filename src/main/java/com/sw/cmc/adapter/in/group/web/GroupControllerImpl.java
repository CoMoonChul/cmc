package com.sw.cmc.adapter.in.group.web;

import com.sw.cmc.adapter.in.group.dto.CreateReqDTO;
import com.sw.cmc.adapter.in.group.dto.CreateResDTO;
import com.sw.cmc.adapter.in.group.dto.GetGroupMemberListResDTO;
import com.sw.cmc.application.port.in.group.GroupUseCase;
import com.sw.cmc.domain.group.GroupDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
    private final ModelMapper modelMapper;

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

        return ResponseEntity.ok(modelMapper.map(groupUseCase.getGroupMemberList(groupDomain), GetGroupMemberListResDTO.class));
    }

}
