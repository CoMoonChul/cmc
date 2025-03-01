package com.sw.cmc.application.service.battle;

import com.sw.cmc.adapter.out.battle.persistence.BattleRepository;
import com.sw.cmc.adapter.out.comment.persistence.CommentRepository;
import com.sw.cmc.application.port.in.battle.BattleUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.battle.BattleDomain;
import com.sw.cmc.entity.Battle;
import com.sw.cmc.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserUtil userUtil;

    @Override
    public BattleDomain selectBattle(Long id) throws Exception {
        Battle found = battleRepository.findByBattleId(id).orElseThrow(() -> new CmcException("BATTLE001"));
        return BattleDomain.builder()
                .battleId(found.getBattleId())
                .title(found.getTitle())
                .content(found.getContent())
                .endTime(found.getEndTime())
                .codeContentLeft(found.getCodeContentLeft())
                .codeContentRight(found.getCodeContentRight())
                .userNum(found.getUser().getUserNum())
                .createdAt(found.getCreatedAt())
                .updatedAt(found.getUpdatedAt())
                .build();
    }

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
    @Transactional
    public BattleDomain updateBattle(BattleDomain battleDomain) throws Exception {
        battleDomain.validateUpdateBattle();
        Battle found = battleRepository.findById(battleDomain.getBattleId())
                .orElseThrow(() -> new CmcException("BATTLE001"));

        // 작성자에 의한 요청이 아닌 경우
        if (!Objects.equals(found.getUser().getUserNum(), userUtil.getAuthenticatedUserNum())) {
            throw new CmcException("BATTLE007");
        }

        // 댓글이 존재할 경우 수정 불가
        if (commentRepository.existsByTargetIdAndCommentTarget(found.getBattleId(), 1)) {
            throw new CmcException("BATTLE008");
        }

        found.setContent(battleDomain.getContent());
        found.setTitle(battleDomain.getTitle());
        found.setEndTime(battleDomain.getEndTime());
        found.setCodeContentLeft(battleDomain.getCodeContentLeft());
        found.setCodeContentRight(battleDomain.getCodeContentRight());

        Battle saved = battleRepository.save(found);
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
    @Transactional
    public BattleDomain deleteBattle(BattleDomain battleDomain) throws Exception {
        Battle found = battleRepository.findById(battleDomain.getBattleId())
                .orElseThrow(() -> new CmcException("BATTLE001"));

        if (!Objects.equals(found.getUser().getUserNum(), userUtil.getAuthenticatedUserNum())) {
            throw new CmcException("BATTLE007");
        }

        battleRepository.deleteById(found.getBattleId());
        return battleDomain;
    }
}
