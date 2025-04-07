package com.sw.cmc.application.service.battle;

import com.sw.cmc.adapter.out.battle.persistence.BattleRepository;
import com.sw.cmc.adapter.out.comment.persistence.CommentRepository;
import com.sw.cmc.adapter.out.vote.persistence.VoteRepository;
import com.sw.cmc.application.port.in.battle.BattleUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.battle.BattleDetailVo;
import com.sw.cmc.domain.battle.BattleDomain;
import com.sw.cmc.domain.battle.BattleListDomain;
import com.sw.cmc.domain.battle.BattleVoteDomain;
import com.sw.cmc.entity.Battle;
import com.sw.cmc.entity.User;
import com.sw.cmc.entity.Vote;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final VoteRepository voteRepository;
    private final UserUtil userUtil;

    @Override
    public BattleDomain selectBattle(Long id) {
        BattleDetailVo found = battleRepository.findBattleDetail(id).orElseThrow(() -> new CmcException("BATTLE001"));
        BattleDomain.BattleDomainBuilder builder = BattleDomain.builder()
                .battleId(found.getBattle().getBattleId())
                .title(found.getBattle().getTitle())
                .content(found.getBattle().getContent())
                .endTime(found.getBattle().getEndTime())
                .codeContentLeft(found.getBattle().getCodeContentLeft())
                .codeContentRight(found.getBattle().getCodeContentRight())
                .codeTypeLeft(found.getBattle().getCodeTypeLeft())
                .codeTypeRight(found.getBattle().getCodeTypeRight())
                .userNum(found.getUserNum())
                .username(found.getUsername())
                .userImg(found.getUserImg())
                .createdAt(found.getBattle().getCreatedAt())
                .updatedAt(found.getBattle().getUpdatedAt())
                .leftVote(found.getLeftVote())
                .rightVote(found.getRightVote())
                .viewCount(found.getViewCount());
        return builder.build();
    }

    @Override
    public BattleListDomain selectBattleList(Integer condition, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Object[]> res = switch (condition) {
            // 최신순
            case 0 -> battleRepository.findAllWithVoteCounts(pageable);
            // 투표많은순
            case 1 -> battleRepository.findAllOrderByVoteCountDesc(pageable);
            // 본인 작성
            case 2 -> {
                if (Objects.isNull(userUtil.getAuthenticatedUserNum())) {
                    throw new CmcException("BATTLE011");
                }
                yield battleRepository.findMyBattles(userUtil.getAuthenticatedUserNum(), pageable);
            }
            // 본인 참여
            case 3 -> {
                if (Objects.isNull(userUtil.getAuthenticatedUserNum())) {
                    throw new CmcException("BATTLE011");
                }
                yield battleRepository.findMyVotedBattles(userUtil.getAuthenticatedUserNum(), pageable);
            }
            default -> throw new CmcException("BATTLE010");
        };
        return convertObjToListDomain(res);
    }

    private BattleListDomain convertObjToListDomain(Page<Object[]> page) {
        List<BattleDomain> battleList = Optional.of(page.getContent())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::toBattleDomain)
                .filter(Objects::nonNull) // null 필터링
                .collect(Collectors.toList());

        return BattleListDomain.builder()
                .pageNumber(Optional.of(page.getNumber()).orElse(0))
                .pageSize(Optional.of(page.getSize()).orElse(0))
                .totalElements((int) page.getTotalElements())
                .totalPages(Optional.of(page.getTotalPages()).orElse(0))
                .battleList(battleList)
                .build();
    }

    private BattleDomain toBattleDomain(Object[] objects) {
        if (objects == null || objects.length < 1 || !(objects[0] instanceof Battle battle)) {
            return null;
        }

        Long leftVoteCount = (Long)objects[1];
        Long rightVoteCount = (Long)objects[2];
        String username = (String)objects[3];
        String userImg = (String)objects[4];
        return BattleDomain.builder()
                .battleId(battle.getBattleId())
                .title(battle.getTitle())
                .content(battle.getContent())
                .endTime(battle.getEndTime())
                .codeContentLeft(battle.getCodeContentLeft())
                .codeContentRight(battle.getCodeContentRight())
                .leftVote(leftVoteCount)
                .rightVote(rightVoteCount)
                .username(username)
                .userImg(userImg)
                .createdAt(battle.getCreatedAt())
                .updatedAt(battle.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public BattleDomain createBattle(BattleDomain battleDomain) {
        battleDomain.validateCreateBattle();
        Battle saving = convertDomainToEntity(battleDomain);
        Battle saved = battleRepository.save(saving);

        return BattleDomain.builder()
                .battleId(saved.getBattleId())
                .build();
    }

    private Battle convertDomainToEntity(BattleDomain battleDomain) {
        User savingUser = new User();
        savingUser.setUserNum(userUtil.getAuthenticatedUserNum());

        Battle saving = new Battle();
        saving.setUser(savingUser);
        saving.setCodeContentLeft(battleDomain.getCodeContentLeft());
        saving.setCodeContentRight(battleDomain.getCodeContentRight());
        saving.setCodeTypeLeft(battleDomain.getCodeTypeLeft());
        saving.setCodeTypeRight(battleDomain.getCodeTypeRight());
        saving.setTitle(battleDomain.getTitle());
        saving.setContent(battleDomain.getContent());
        saving.setEndTime(battleDomain.getEndTime());
        return saving;
    }

    @Override
    @Transactional
    public BattleDomain updateBattle(BattleDomain battleDomain) {
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
        found.setCodeTypeLeft(battleDomain.getCodeTypeLeft());
        found.setCodeTypeRight(battleDomain.getCodeTypeRight());

        Battle saved = battleRepository.save(found);
        return BattleDomain.builder()
                .battleId(saved.getBattleId())
                .build();
    }

    @Override
    @Transactional
    public BattleVoteDomain updateVoteBattle(BattleVoteDomain battleVoteDomain) {
        battleVoteDomain.validateUpdateBattleVote();
        Vote vote = voteRepository.findByUser_UserNumAndBattleId(userUtil.getAuthenticatedUserNum(), battleVoteDomain.getBattleId())
            .map(found -> {
                found.setVoteValue(battleVoteDomain.getVoteValue());
                return found;
            })
            .orElseGet(() -> {
                User savingUser = new User();
                savingUser.setUserNum(userUtil.getAuthenticatedUserNum());
                Vote saving = new Vote();
                saving.setUser(savingUser);
                saving.setBattleId(battleVoteDomain.getBattleId());
                saving.setVoteValue(battleVoteDomain.getVoteValue());
                return saving;
            });
        Vote saved = voteRepository.save(vote);
        return BattleVoteDomain.builder()
                .battleId(saved.getBattleId())
                .voteValue(saved.getVoteValue())
                .build();
    }

    @Override
    @Transactional
    public BattleVoteDomain deleteVoteBattle(BattleVoteDomain battleVoteDomain) {
        Vote found = voteRepository.findById(battleVoteDomain.getBattleId())
                .orElseThrow(() -> new CmcException("BATTLE001"));
        if (!Objects.equals(found.getUser().getUserNum(), userUtil.getAuthenticatedUserNum())) {
            throw new CmcException("BATTLE007");
        }

        voteRepository.deleteById(found.getBattleId());
        return battleVoteDomain;
    }

    @Override
    @Transactional
    public BattleDomain deleteBattle(BattleDomain battleDomain) {
        Battle found = battleRepository.findById(battleDomain.getBattleId())
                .orElseThrow(() -> new CmcException("BATTLE001"));

        if (!Objects.equals(found.getUser().getUserNum(), userUtil.getAuthenticatedUserNum())) {
            throw new CmcException("BATTLE007");
        }

        battleRepository.deleteById(found.getBattleId());
        return battleDomain;
    }

    @Override
    public BattleDomain selectBattleVoteState(Long id) throws Exception {
        BattleDetailVo found = battleRepository.findBattleDetail(id).orElseThrow(() -> new CmcException("BATTLE001"));
        BattleDomain.BattleDomainBuilder builder = BattleDomain.builder()
                .battleId(found.getBattle().getBattleId());

        Long userNum = userUtil.getAuthenticatedUserNum();
        if (userNum != null) {
            voteRepository.findByUser_UserNumAndBattleId(userNum, found.getBattle().getBattleId())
                    .ifPresent(vote -> builder.voteValue(vote.getVoteValue()));
        }
        return builder.build();
    }
}
