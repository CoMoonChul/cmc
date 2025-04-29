package com.sw.cmc.adapter.in.ai.web;

import com.sw.cmc.adapter.in.ai.dto.SelectAICommentResDTO;
import com.sw.cmc.application.port.in.ai.AIUseCase;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.ai.web
 * fileName       : AiControllerImpl
 * author         : ihw
 * date           : 2025. 4. 30.
 * description    : ai api controller
 */
@RestController
@RequiredArgsConstructor
public class AiControllerImpl implements AiControllerApi{

    private final AIUseCase aiUseCase;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<SelectAICommentResDTO> selectAIComment(Long id) throws Exception {
        return ResponseEntity.ok(modelMapper.map(aiUseCase.selectAIComment(id), SelectAICommentResDTO.class));
    }
}
