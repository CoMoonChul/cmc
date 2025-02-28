package com.sw.cmc.adapter.in.battle.web;

import com.sw.cmc.adapter.in.battle.dto.CreateBattleReqDTO;
import com.sw.cmc.adapter.in.battle.dto.CreateBattleResDTO;
import com.sw.cmc.adapter.in.battle.dto.DeleteBattleReqDTO;
import com.sw.cmc.adapter.in.battle.dto.DeleteBattleResDTO;
import com.sw.cmc.application.port.in.battle.BattleUseCase;
import com.sw.cmc.domain.battle.BattleDomain;
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
    public ResponseEntity<CreateBattleResDTO> createBattle(CreateBattleReqDTO createBattleReqDTO) throws Exception {
        BattleDomain battleDomain = BattleDomain.builder()
                .title(createBattleReqDTO.getTitle())
                .content(createBattleReqDTO.getContent())
                .codeOneContent(createBattleReqDTO.getCodeOneContent())
                .codeTwoContent(createBattleReqDTO.getCodeTwoContent())
                .endTime(createBattleReqDTO.getEndTime())
                .build();
        return ResponseEntity.ok(modelMapper.map(battleUseCase.createBattle(battleDomain), CreateBattleResDTO.class));
    }

    @Override
    public ResponseEntity<DeleteBattleResDTO> deleteBattle(DeleteBattleReqDTO deleteBattleReqDTO) throws Exception {
        BattleDomain battleDomain = BattleDomain.builder()
                .battleId(deleteBattleReqDTO.getBattleId())
                .build();
        return ResponseEntity.ok(modelMapper.map(battleUseCase.deleteBattle(battleDomain), DeleteBattleResDTO.class));
    }
}
