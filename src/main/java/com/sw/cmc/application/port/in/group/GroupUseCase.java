package com.sw.cmc.application.port.in.group;

import com.sw.cmc.domain.group.GroupDomain;

import java.util.List;

/**
 * packageName    : com.sw.cmc.application.port.in.group
 * fileName       : GroupUseCase
 * author         : SungSuHan
 * date           : 2025-03-03
 * description    :
 */
public interface GroupUseCase {
    String create(GroupDomain groupDomain) throws Exception;

    GroupDomain getGroupMemberList(GroupDomain groupDomain) throws Exception;

    String delete(GroupDomain groupDomain) throws Exception;

    String invite(GroupDomain groupDomain) throws Exception;

    String expel(GroupDomain groupDomain) throws Exception;

    GroupDomain getMyGroupList() throws Exception;
}
