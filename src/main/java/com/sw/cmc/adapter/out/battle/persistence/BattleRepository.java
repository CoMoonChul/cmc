package com.sw.cmc.adapter.out.battle.persistence;

import com.sw.cmc.domain.battle.BattleDetailVo;
import com.sw.cmc.entity.Battle;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT new com.sw.cmc.domain.battle.BattleDetailVo( " +
            "b, u.username, " +
            "(SELECT COUNT(v) FROM Vote v WHERE v.battleId = b.battleId AND v.voteValue = 0), " +
            "(SELECT COUNT(v) FROM Vote v WHERE v.battleId = b.battleId AND v.voteValue = 1), " +
            "(SELECT bv.viewCount FROM BattleView bv WHERE bv.battleId = b.battleId) ) " +
            "FROM Battle b " +
            "LEFT JOIN b.user u " +
            "WHERE b.battleId = :battleId")
    BattleDetailVo findBattleDetail(@Param("battleId") Long battleId);

    /**
     * methodName : findByBattleId
     * author : IM HYUN WOO
     * description : battleId를 이용해 유저와 조인하여 조회
     *
     * @param battleId long
     * @return optional
     */
    @Query("SELECT b FROM Battle b JOIN FETCH b.user " +
            "WHERE b.battleId = :battleId")
    Optional<Battle> findByBattleId(Long battleId);

    /**
     * methodName : findAllWithVoteCounts
     * author : IM HYUN WOO
     * description : 최신순 조회
     *
     * @param pageable Pageable
     * @return page
     */
    @Query("""
        SELECT b,
               COUNT(CASE WHEN v.voteValue = 0 THEN 1 END) AS leftVoteCount,
               COUNT(CASE WHEN v.voteValue = 1 THEN 1 END) AS rightVoteCount
        FROM Battle b
        LEFT JOIN Vote v ON b.battleId = v.battleId
        GROUP BY b
    """)
    Page<Object[]> findAllWithVoteCounts(Pageable pageable);

    /**
     * methodName : findAllOrderByVoteCountDescAndCreatedAtDesc
     * author : IM HYUN WOO
     * description : 투표 많은 순 조회
     *
     * @return list
     */
    @Query("""
        SELECT b,
            COUNT(CASE WHEN v.voteValue = 0 THEN 1 END) AS leftVoteCount,
            COUNT(CASE WHEN v.voteValue = 1 THEN 1 END) AS rightVoteCount
        FROM Battle b
        LEFT JOIN Vote v ON b.battleId = v.battleId
        GROUP BY b.battleId
        ORDER BY COUNT(v.voteId) DESC
    """)
    Page<Object[]> findAllOrderByVoteCountDesc(Pageable pageable);

    /**
     * methodName : findMyVotedBattles
     * author : IM HYUN WOO
     * description : 내가 투표한 배틀 조회
     *
     * @param userNum Long
     * @param pageable Pageable
     * @return list
     */
    @Query("""
        SELECT b,
            COUNT(CASE WHEN v.voteValue = 0 THEN 1 END) AS leftVoteCount,
            COUNT(CASE WHEN v.voteValue = 1 THEN 1 END) AS rightVoteCount
        FROM Battle b
        JOIN Vote v ON b.battleId = v.battleId
        WHERE v.user.userNum = :userNum
        GROUP BY b.battleId
    """)
    Page<Object[]> findMyVotedBattles(Long userNum, Pageable pageable);

    /**
     * methodName : findMyBattles
     * author : IM HYUN WOO
     * description : 본인이 작성한 배틀
     *
     * @param userNum Long
     * @param pageable Pageable
     * @return page
     */
    @Query("""
        SELECT b,
            COUNT(CASE WHEN v.voteValue = 0 THEN 1 END) AS leftVoteCount,
            COUNT(CASE WHEN v.voteValue = 1 THEN 1 END) AS rightVoteCount
        FROM Battle b
        LEFT JOIN Vote v ON b.battleId = v.battleId
        WHERE b.user.userNum = :userNum
        GROUP BY b.battleId
    """)
    Page<Object[]> findMyBattles(Long userNum, Pageable pageable);
}
