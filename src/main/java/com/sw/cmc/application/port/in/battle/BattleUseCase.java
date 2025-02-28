package com.sw.cmc.application.port.in.battle;

import com.sw.cmc.domain.battle.BattleDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.battle
 * fileName       : BattleUseCase
 * author         : ihw
 * date           : 2025. 2. 28.
 * description    : Battle usecase
 */
public interface BattleUseCase {
    /**
     * methodName : createBattle
     * author : IM HYUN WOO
     * description : 배틀 생성
     *
     * @param battle domain
     * @return battle domain
     * @throws Exception the exception
     */
    BattleDomain createBattle(BattleDomain battleDomain) throws Exception;

    /**
     * methodName : deleteBattle
     * author : IM HYUN WOO
     * description : 배틀 삭제
     *
     * @param battle domain
     * @return battle domain
     * @throws Exception the exception
     */
    BattleDomain deleteBattle(BattleDomain battleDomain) throws Exception;
}
