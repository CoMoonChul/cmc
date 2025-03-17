package com.sw.cmc.adapter.in.battle.web;

import com.sw.cmc.adapter.in.battle.dto.*;
import com.sw.cmc.application.port.in.battle.BattleUseCase;
import com.sw.cmc.domain.battle.BattleDomain;
import com.sw.cmc.domain.battle.BattleVoteDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.battle.web
 * fileName       : BattleControllerImpl
 * author         : ihw
 * date           : 2025. 2. 28.
 * description    : Battle controller
 */
@RestController
@RequiredArgsConstructor
public class BattleControllerImpl implements BattleControllerApi {

    private final BattleUseCase battleUseCase;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<SelectBattleResDTO> selectBattle(Long id) throws Exception {
        return ResponseEntity.ok(modelMapper.map(battleUseCase.selectBattle(id), SelectBattleResDTO.class));
    }

    @Override
    public ResponseEntity<SelectBattleListResDTO> selectBattleList(Integer condition, Integer page, Integer size) throws Exception {
        return ResponseEntity.ok(modelMapper.map(battleUseCase.selectBattleList(condition, page, size), SelectBattleListResDTO.class));
    }

    @Override
    public ResponseEntity<CreateBattleResDTO> createBattle(CreateBattleReqDTO createBattleReqDTO) throws Exception {
        BattleDomain battleDomain = BattleDomain.builder()
                .title(createBattleReqDTO.getTitle())
                .content(createBattleReqDTO.getContent())
                .codeContentLeft(createBattleReqDTO.getCodeContentLeft())
                .codeContentRight(createBattleReqDTO.getCodeContentRight())
                .endTime(createBattleReqDTO.getEndTime())
                .build();
        return ResponseEntity.ok(modelMapper.map(battleUseCase.createBattle(battleDomain), CreateBattleResDTO.class));
    }

    @Override
    public ResponseEntity<UpdateBattleResDTO> updateBattle(UpdateBattleReqDTO updateBattleReqDTO) throws Exception {
        BattleDomain battleDomain = BattleDomain.builder()
                .battleId(updateBattleReqDTO.getBattleId())
                .title(updateBattleReqDTO.getTitle())
                .content(updateBattleReqDTO.getContent())
                .codeContentLeft(updateBattleReqDTO.getCodeContentLeft())
                .codeContentRight(updateBattleReqDTO.getCodeContentRight())
                .endTime(updateBattleReqDTO.getEndTime())
                .build();
        return ResponseEntity.ok(modelMapper.map(battleUseCase.updateBattle(battleDomain), UpdateBattleResDTO.class));
    }

    @Override
    public ResponseEntity<UpdateVoteBattleResDTO> updateVoteBattle(UpdateVoteBattleReqDTO updateVoteBattleReqDTO) throws Exception {
        BattleVoteDomain battleVoteDomain = BattleVoteDomain.builder()
                .battleId(updateVoteBattleReqDTO.getBattleId())
                .voteValue(updateVoteBattleReqDTO.getVoteValue())
                .build();
        return ResponseEntity.ok(modelMapper.map(battleUseCase.updateVoteBattle(battleVoteDomain), UpdateVoteBattleResDTO.class));
    }

    @Override
    public ResponseEntity<DeleteVoteBattleResDTO> deleteVoteBattle(DeleteVoteBattleReqDTO deleteVoteBattleReqDTO) throws Exception {
        BattleVoteDomain battleVoteDomain = BattleVoteDomain.builder()
                .battleId(deleteVoteBattleReqDTO.getBattleId())
                .build();
        return ResponseEntity.ok(modelMapper.map(battleUseCase.deleteVoteBattle(battleVoteDomain), DeleteVoteBattleResDTO.class));
    }

    @Override
    public ResponseEntity<DeleteBattleResDTO> deleteBattle(DeleteBattleReqDTO deleteBattleReqDTO) throws Exception {
        BattleDomain battleDomain = BattleDomain.builder()
                .battleId(deleteBattleReqDTO.getBattleId())
                .build();
        return ResponseEntity.ok(modelMapper.map(battleUseCase.deleteBattle(battleDomain), DeleteBattleResDTO.class));
    }

    @Override
    public ResponseEntity<SelectBattleVoteStateResDTO> selectBattleVoteState(Long id) throws Exception {
        return ResponseEntity.ok(modelMapper.map(battleUseCase.selectBattleVoteState(id), SelectBattleVoteStateResDTO.class));
    }
}
