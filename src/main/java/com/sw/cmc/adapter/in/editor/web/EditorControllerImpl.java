package com.sw.cmc.adapter.in.editor.web;

import com.sw.cmc.adapter.in.editor.dto.*;
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

    @Override
    public ResponseEntity<UpdateEditorResDTO> updateEditor(UpdateEditorReqDTO updateEditorReqDTO) throws Exception {
        EditorDomain editorDomain = EditorDomain.builder()
                .codeEditNum(updateEditorReqDTO.getCodeEditNum())
                .content(updateEditorReqDTO.getContent())
                .language(updateEditorReqDTO.getLanguage())
                .build();
        return ResponseEntity.ok(modelMapper.map(editorUseCase.updateEditor(editorDomain), UpdateEditorResDTO.class));
    }

    @Override
    public ResponseEntity<DeleteEditorResDTO> deleteEditor(DeleteEditorReqDTO deleteEditorReqDTO) throws Exception {
        EditorDomain editorDomain = EditorDomain.builder()
                .codeEditNum(deleteEditorReqDTO.getCodeEditNum())
                .build();
        return ResponseEntity.ok(modelMapper.map(editorUseCase.deleteEditor(editorDomain), DeleteEditorResDTO.class));
    }
}
