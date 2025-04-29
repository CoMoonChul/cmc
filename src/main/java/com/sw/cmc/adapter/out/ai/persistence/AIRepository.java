package com.sw.cmc.adapter.out.ai.persistence;

import com.sw.cmc.entity.AIComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * packageName    : com.sw.cmc.adapter.out.ai.persistence
 * fileName       : AIRepository
 * author         : ihw
 * date           : 2025. 4. 29.
 * description    : ai comment repository
 */
@Repository
public interface AIRepository extends JpaRepository<AIComment, Long> {
    Optional<AIComment> findByTargetId(Long id);
}
