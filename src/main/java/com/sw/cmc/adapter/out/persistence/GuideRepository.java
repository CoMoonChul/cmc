package com.sw.cmc.adapter.out.persistence;

import com.sw.cmc.adapter.in.guide.dto.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.sw.cmc.adapter.out.persistence
 * fileName       : GuideRepository
 * author         : ihw
 * date           : 2025. 2. 4.
 * description    : Guide Jpa Repository
 */
@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
}