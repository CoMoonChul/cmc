package com.sw.cmc.adapter.out.battle.persistence;

import com.sw.cmc.entity.Battle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * packageName    : com.sw.cmc.adapter.out.battle.persistence
 * fileName       : BattleRepository
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 배틀 repository
 */
@Repository
public interface BattleRepository extends JpaRepository<Battle, Long>  {


    /**
     * methodName : findByBattleId
     * author : IM HYUN WOO
     * description : battleId를 이용해 유저와 조인하여 조회
     *
     * @param a long
     * @return optional
     */
    @Query("SELECT b FROM Battle b JOIN FETCH b.user " +
            "WHERE b.battleId = :battleId")
    Optional<Battle> findByBattleId(Long battleId);
}
