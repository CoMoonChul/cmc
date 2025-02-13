package com.sw.cmc.adapter.out.persistence.edt;

import com.sw.cmc.domain.edt.Editor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.sw.cmc.adapter.out.persistence.repositroy
 * fileName       : EditorRepository
 * author         : Ko
 * date           : 2025-02-09
 * description    :
 */
public interface EditorRepository extends JpaRepository<Editor, Long> {
    // 특정 LiveCoding의 Editor 조회
    Optional<Editor> findByLiveCodingId(Long roomId);
}
