package com.sw.cmc.adapter.out.view.persistence;

import com.sw.cmc.adapter.in.view.dto.BattleView;
import com.sw.cmc.adapter.in.view.dto.ReviewView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.sw.cmc.adapter.out.view.persistence
 * fileName       : BattleViewRepository
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 배틀 조회수 repository
 */
@Repository
public interface BattleViewRepository extends JpaRepository<BattleView, Long> {
}
