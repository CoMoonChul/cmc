package com.sw.cmc.adapter.out.user.persistence;

import com.sw.cmc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * packageName    : com.sw.cmc.adapter.out.persistence.user
 * fileName       : LoginRepository
 * author         : SungSuHan
 * date           : 2025-02-11
 * description    :
 */
@Repository
public interface LoginRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
}
