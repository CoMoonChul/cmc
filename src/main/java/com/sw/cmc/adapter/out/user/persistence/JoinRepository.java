package com.sw.cmc.adapter.out.user.persistence;

import com.sw.cmc.adapter.in.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
