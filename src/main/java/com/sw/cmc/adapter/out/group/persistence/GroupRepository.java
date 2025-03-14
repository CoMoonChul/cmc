package com.sw.cmc.adapter.out.group.persistence;

import com.sw.cmc.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.sw.cmc.adapter.out.group.persistence
 * fileName       : GroupRepository
 * author         : SungSuHan
 * date           : 2025-03-03
 * description    :
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByGroupName(String groupName);

    Optional<Group> findByGroupId(Long groupId);

    void deleteByGroupId(Long groupId);
}
