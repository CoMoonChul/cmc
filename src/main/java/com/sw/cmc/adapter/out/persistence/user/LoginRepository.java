package com.sw.cmc.adapter.out.persistence.user;

import com.sw.cmc.adapter.in.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.sw.cmc.adapter.out.persistence.user
 * fileName       : LoginRepository
 * author         : SungSuHan
 * date           : 2025-02-11
 * description    :
 */
@Repository
public interface LoginRepository extends JpaRepository<User, Long> {
}
