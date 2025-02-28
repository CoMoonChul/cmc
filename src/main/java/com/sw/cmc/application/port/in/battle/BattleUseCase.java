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
    BattleDomain createBattle(BattleDomain battleDomain) throws Exception;
}
