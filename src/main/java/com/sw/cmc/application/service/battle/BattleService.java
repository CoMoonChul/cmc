package com.sw.cmc.application.service.battle;

import com.sw.cmc.adapter.out.battle.persistence.BattleRepository;
import com.sw.cmc.application.port.in.battle.BattleUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.battle.BattleDomain;
import com.sw.cmc.entity.Battle;
import com.sw.cmc.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.sw.cmc.application.service.battle
 * fileName       : BattleService
 * author         : ihw
 * date           : 2025. 2. 28.
 * description    : Battle Service
 */
@Service
@RequiredArgsConstructor
public class BattleService implements BattleUseCase {

    private final BattleRepository battleRepository;
    private final UserUtil userUtil;

    @Override
    @Transactional
    public BattleDomain createBattle(BattleDomain battleDomain) throws Exception {
        battleDomain.validateCreateBattle();
        User savingUser = new User();
        savingUser.setUserNum(userUtil.getAuthenticatedUserNum());

        Battle saving = new Battle();
        saving.setUser(savingUser);
        saving.setCodeContentLeft(battleDomain.getCodeContentLeft());
        saving.setCodeContentRight(battleDomain.getCodeContentRight());
        saving.setTitle(battleDomain.getTitle());
        saving.setContent(battleDomain.getContent());
        saving.setEndTime(battleDomain.getEndTime());
        Battle saved = battleRepository.save(saving);

        return BattleDomain.builder()
                .battleId(saved.getBattleId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .endTime(saved.getEndTime())
                .codeContentLeft(saved.getCodeContentLeft())
                .codeContentRight(saved.getCodeContentRight())
                .userNum(saved.getUser().getUserNum())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    public BattleDomain deleteBattle(BattleDomain battleDomain) throws Exception {
        Battle found = battleRepository.findById(battleDomain.getBattleId())
                .orElseThrow(() -> new CmcException("BATTLE007"));

        battleRepository.deleteById(found.getBattleId());
        return battleDomain;
    }
}
