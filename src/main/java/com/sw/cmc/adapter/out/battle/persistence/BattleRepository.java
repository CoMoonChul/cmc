package com.sw.cmc.adapter.out.battle.persistence;

import com.sw.cmc.adapter.in.battle.dto.Battle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.sw.cmc.adapter.out.battle.persistence
 * fileName       : BattleRepository
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 배틀 repository
 */
@Repository
public interface BattleRepository extends JpaRepository<Battle, Long>  {
}
