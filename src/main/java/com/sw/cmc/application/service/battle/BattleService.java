package com.sw.cmc.application.service.battle;

import com.sw.cmc.adapter.out.battle.persistence.BattleRepository;
import com.sw.cmc.adapter.out.editor.persistence.EditorRepository;
import com.sw.cmc.application.port.in.battle.BattleUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.battle.BattleDomain;
import com.sw.cmc.entity.Battle;
import com.sw.cmc.entity.Editor;
import com.sw.cmc.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

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
    private final EditorRepository editorRepository;
    private final UserUtil userUtil;

    @Override
    @Transactional
    public BattleDomain createBattle(BattleDomain battleDomain) throws Exception {
        battleDomain.validateCreateBattle();
        User savingUser = new User();
        savingUser.setUserNum(userUtil.getAuthenticatedUserNum());

        Editor savingEditorOne = new Editor();
        savingEditorOne.setContent(battleDomain.getCodeOneContent());
        savingEditorOne.setUser(savingUser);
        Editor savingEditorTwo = new Editor();
        savingEditorTwo.setContent(battleDomain.getCodeTwoContent());
        savingEditorTwo.setUser(savingUser);
        List<Editor> savedList = editorRepository.saveAll(Arrays.asList(savingEditorOne, savingEditorTwo));


        if (CollectionUtils.isEmpty(savedList) || savedList.size() < 2) {
            throw new CmcException("BATTLE001");
        }

        Battle saving = new Battle();
        saving.setUser(savingUser);
        saving.setEditorOne(savedList.get(0));
        saving.setEditorTwo(savedList.get(1));
        saving.setTitle(battleDomain.getTitle());
        saving.setContent(battleDomain.getContent());
        saving.setEndTime(battleDomain.getEndTime());
        Battle saved = battleRepository.save(saving);

        return BattleDomain.builder()
                .battleId(saved.getBattleId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .endTime(saved.getEndTime())
                .codeOneNum(saved.getEditorOne().getCodeEditNum())
                .codeOneContent(saved.getEditorOne().getContent())
                .codeTwoNum(saved.getEditorTwo().getCodeEditNum())
                .codeTwoContent(saved.getEditorTwo().getContent())
                .userNum(saved.getUser().getUserNum())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }
}
