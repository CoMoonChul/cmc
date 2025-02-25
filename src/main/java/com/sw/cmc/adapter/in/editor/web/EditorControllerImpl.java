package com.sw.cmc.adapter.in.editor.web;

import com.sw.cmc.adapter.in.editor.dto.CreateEditorReqDTO;
import com.sw.cmc.adapter.in.editor.dto.CreateEditorResDTO;
import com.sw.cmc.application.port.in.editor.EditorUseCase;
import com.sw.cmc.domain.editor.EditorDomain;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.editor.web
 * fileName       : EditorControllerImpl
 * author         : An Seung Gi
 * date           : 2025-02-25
 * description    : Editor controller
 */
@RestController
@RequiredArgsConstructor
public class EditorControllerImpl implements EditorControllerApi{

    private final ModelMapper modelMapper;
    private final EditorUseCase editorUseCase;

    @Override
    public ResponseEntity<CreateEditorResDTO> createEditor(CreateEditorReqDTO createEditorReqDTO) throws Exception {
        EditorDomain editorDomain = EditorDomain.builder()
                .content(createEditorReqDTO.getContent())
                .language(createEditorReqDTO.getLanguage())
                .build();
        return ResponseEntity.ok(modelMapper.map(editorUseCase.createEditor(editorDomain), CreateEditorResDTO.class));
    }
}
