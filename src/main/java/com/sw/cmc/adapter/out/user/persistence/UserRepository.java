package com.sw.cmc.adapter.out.user.persistence;

import com.sw.cmc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *packageName    : com.sw.cmc.adapter.out.user.persistence
 * fileName       : UserRepository
 * author         : SungSuHan
 * date           : 2025-02-27
 * description    :
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserNum(Long userNum);
}
