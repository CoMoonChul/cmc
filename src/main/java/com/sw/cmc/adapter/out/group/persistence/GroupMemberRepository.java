package com.sw.cmc.adapter.out.group.persistence;

import com.sw.cmc.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * methodName : findByUserNum_Group
     * author : IM HYUN WOO
     * description :
     *
     * @param userNum Long
     * @return list
     */
    @Query("SELECT gm FROM GroupMember gm " +
            "JOIN FETCH gm.group g " +
            "WHERE gm.user.userNum = :userNum")
    List<GroupMember> findByUserNum_Group(Long userNum);

    /**
     * methodName : findDistinctUserNumsByGroupIds
     * author : IM HYUN WOO
     * description : 그룹 id 배열이 주어졌을 때, userNum 배열 반환
     *
     * @param groupIds List<Long>
     * @return list
     */
    @Query("SELECT DISTINCT gm.user.userNum FROM GroupMember gm WHERE gm.group.groupId IN :groupIds")
    List<Long> findDistinctUserNumsByGroupIds(@Param("groupIds") List<Long> groupIds);
}
