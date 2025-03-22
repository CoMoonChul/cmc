package com.sw.cmc.application.port.in.battle;

import com.sw.cmc.domain.battle.BattleDomain;
import com.sw.cmc.domain.battle.BattleListDomain;
import com.sw.cmc.domain.battle.BattleVoteDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.battle
 * fileName       : BattleUseCase
 * author         : ihw
 * date           : 2025. 2. 28.
 * description    : Battle usecase
 */
public interface BattleUseCase {

    /**
     * methodName : selectBattle
     * author : IM HYUN WOO
     * description : 배틀 단건 조회
     *
     * @param id Long
     * @return battle domain
     * @throws Exception the exception
     */
    BattleDomain selectBattle(Long id) throws Exception;

    /**
     * methodName : selectBattleList
     * author : IM HYUN WOO
     * description : 배틀 리스트 조회
     *
     * @param condition Integer
     * @param page Integer
     * @param size Integer
     * @return battle domain
     * @throws Exception the exception
     */
    BattleListDomain selectBattleList(Integer condition, Integer page, Integer size) throws Exception;

    /**
     * methodName : createBattle
     * author : IM HYUN WOO
     * description : 배틀 생성
     *
     * @param battleDomain BattleDomain
     * @return battle domain
     * @throws Exception the exception
     */
    BattleDomain createBattle(BattleDomain battleDomain) throws Exception;

    /**
     * methodName : updateBattle
     * author : IM HYUN WOO
     * description : 배틀 수정
     *
     * @param battleDomain BattleDomain
     * @return battle domain
     * @throws Exception the exception
     */
    BattleDomain updateBattle(BattleDomain battleDomain) throws Exception;

    /**
     * methodName : voteBattle
     * author : IM HYUN WOO
     * description : 배틀 투표
     *
     * @param battleVoteDomain BattleVoteDomain
     * @return battle vote domain
     * @throws Exception the exception
     */
    BattleVoteDomain updateVoteBattle(BattleVoteDomain battleVoteDomain) throws Exception;

    /**
     * methodName : deleteVoteBattle
     * author : IM HYUN WOO
     * description : 배틀 투표 삭제
     *
     * @param battleVoteDomain BattleVoteDomain
     * @return battle vote domain
     * @throws Exception the exception
     */
    BattleVoteDomain deleteVoteBattle(BattleVoteDomain battleVoteDomain) throws Exception;

    /**
     * methodName : deleteBattle
     * author : IM HYUN WOO
     * description : 배틀 삭제
     *
     * @param battleDomain BattleDomain
     * @return battle domain
     * @throws Exception the exception
     */
    BattleDomain deleteBattle(BattleDomain battleDomain) throws Exception;

    /**
     * methodName : selectBattleVoteState
     * author : IM HYUN WOO
     * description : 배틀 투표 상태 조회
     *
     * @param id Long
     * @return battle domain
     * @throws Exception the exception
     */
    BattleDomain selectBattleVoteState(Long id) throws Exception;
}
