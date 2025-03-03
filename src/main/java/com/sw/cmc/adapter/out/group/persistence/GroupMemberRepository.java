package com.sw.cmc.adapter.out.group.persistence;

import com.sw.cmc.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * packageName    : com.sw.cmc.adapter.out.group.persistence
 * fileName       : GroupMemberRepository
 * author         : swhanssu
 * date           : 2025-03-03
 * description    :
 */
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByGroup_GroupId(Long groupId);
}
