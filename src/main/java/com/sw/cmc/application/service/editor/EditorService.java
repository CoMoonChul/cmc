package com.sw.cmc.application.service.editor;

import com.sw.cmc.adapter.out.editor.persistence.EditorRepository;
import com.sw.cmc.application.port.in.editor.EditorUseCase;
import com.sw.cmc.common.util.UserUtil;
import com.sw.cmc.domain.editor.EditorDomain;
import com.sw.cmc.entity.Editor;
import com.sw.cmc.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : com.sw.cmc.application.service.editor
 * fileName       : EditorService
 * author         : An Seung Gi
 * date           : 2025-02-25
 * description    : Editor Service
 */
@Service
@RequiredArgsConstructor
public class EditorService implements EditorUseCase {

    private final ModelMapper modelMapper;
    private final UserUtil userUtil;
    private final EditorRepository editorRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public EditorDomain createEditor(EditorDomain editorDomain) throws Exception {
        User savingUser = new User();
        savingUser.setUserNum(userUtil.getAuthenticatedUserNum());
        Editor editor = modelMapper.map(editorDomain, Editor.class);
        editor.setUser(savingUser);
        Editor saved = editorRepository.save(editor);
        entityManager.refresh(saved);
        return EditorDomain.builder()
                .codeEditNum(saved.getCodeEditNum())
                .content(saved.getContent())
                .userNum(saved.getUser().getUserNum())
                .language(saved.getLanguage())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }
}
