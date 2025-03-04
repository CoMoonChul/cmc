package com.sw.cmc.adapter.out.vote.persistence;

import com.sw.cmc.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * packageName    : com.sw.cmc.adapter.out.vote.persistence
 * fileName       : VoteRepository
 * author         : ihw
 * date           : 2025. 3. 1.
 * description    : vote repository
 */
@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    /**
     * methodName : findByUser_UserNumAndBattleId
     * author : IM HYUN WOO
     * description : usernum과 battleId를 이용한 조회
     *
     * @param user   num
     * @param battle id
     * @return optional
     */
    Optional<Vote> findByUser_UserNumAndBattleId(Long userNum, Long battleId);
}
