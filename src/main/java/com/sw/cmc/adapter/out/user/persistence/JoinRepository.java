package com.sw.cmc.adapter.out.user.persistence;

import com.sw.cmc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.sw.cmc.adapter.out.persistence.user
 * fileName       : JoinRepository
 * author         : SungSuHan
 * date           : 2025-02-11
 * description    :
 */
@Repository
public interface JoinRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);

    boolean existsByUsername(String username);
}
