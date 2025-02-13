package com.sw.cmc.application.service;

import com.sw.cmc.adapter.out.persistence.repositroy.EditorRepository;
import com.sw.cmc.adapter.out.persistence.repositroy.LiveCodingRoomRepository;
import com.sw.cmc.domain.Editor;
import com.sw.cmc.domain.lcd.LiveCodingRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * packageName    : com.sw.cmc.application.service
 * fileName       : EditorService
 * author         : Ko
 * date           : 2025-02-09
 * description    :
 */
@Service
@RequiredArgsConstructor
public class EditorService {
    private final EditorRepository editorRepository;
    private final LiveCodingRoomRepository liveCodingRoomRepository;

    // 특정 LiveCodingRoom에 해당하는 Editor 조회
    public Optional<Editor> getEditorByRoomId(Long roomId) {
        return editorRepository.findByLiveCodingRoomId(roomId);
    }

    // 특정 LiveCodingRoom에 해당하는 Editor 내용 업데이트
    public Editor updateEditorForRoom(Long roomId, Editor editorData) {
        LiveCodingRoom liveCodingRoom = liveCodingRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        editorData.setLiveCodingRoom(liveCodingRoom);
        return editorRepository.save(editorData);
    }

    // 코드 수정 내용 업데이트
    public Editor updateEditorCode(Long roomId, String newCode) {
        Editor editor = editorRepository.findByLiveCodingRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Editor not found"));
        editor.setCode(newCode);
        return editorRepository.save(editor);
    }
}
