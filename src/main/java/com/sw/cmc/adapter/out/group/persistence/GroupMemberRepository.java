package com.sw.cmc.adapter.out.group.persistence;

import com.sw.cmc.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.sw.cmc.adapter.out.group.persistence
 * fileName       : GroupMemberRepository
 * author         : SungSuHan
 * date           : 2025-03-03
 * description    :
 */
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByGroup_GroupId(Long groupId);

    int countByGroup_GroupId(Long groupId);

    Optional<GroupMember> findByGroup_GroupIdAndUser_UserNum(Long groupId, Long userNum);

    @Query("SELECT gm FROM GroupMember gm JOIN FETCH gm.user WHERE gm.group.groupId = :groupId")
    List<GroupMember> findByGroupIdWithUser(Long groupId);
}
