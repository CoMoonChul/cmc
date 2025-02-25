package com.sw.cmc.application.port.in.editor;

import com.sw.cmc.domain.editor.EditorDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.editor
 * fileName       : EditorUseCase
 * author         : dkstm
 * date           : 2025-02-25
 * description    :
 */
public interface EditorUseCase {

    /**
     * methodName : createEditor
     * author : AN SEUNG GI
     * description : 에디터 등록
     *
     * @param editor domain
     * @return editor domain
     * @throws Exception the exception
     */
    EditorDomain createEditor(EditorDomain editorDomain) throws Exception;

    /**
     * methodName : updateEditor
     * author : AN SEUNG GI
     * description : 에디터 수정
     *
     * @param editor domain
     * @return editor domain
     * @throws Exception the exception
     */
    EditorDomain updateEditor(EditorDomain editorDomain) throws Exception;

    /**
     * methodName : deleteEditor
     * author : AN SEUNG GI
     * description : 에디터 삭제
     *
     * @param editor domain
     * @return editor domain
     * @throws Exception the exception
     */
    EditorDomain deleteEditor(EditorDomain editorDomain) throws Exception;
}
