package com.sw.cmc.domain.group;

import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.entity.GroupMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * packageName    : com.sw.cmc.domain.group
 * fileName       : GroupDomain
 * author         : SungSuHan
 * date           : 2025-03-03
 * description    :
 */
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GroupDomain {
    private long groupId;
    private String groupName;

    private long groupMemberId;
    private long userNum;
    private String groupRole;

    private String resultMessage;

    private List<GroupMember> members;

    // Validation
    public static void validateGroupName(String groupName) {
        if (StringUtils.length(groupName) < 2 || StringUtils.length(groupName) > 10) {
            throw new CmcException("USER020");
        }
    }
}
