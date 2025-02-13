package com.sw.cmc.application.service.edt;

import com.sw.cmc.adapter.out.persistence.edt.EditorRepository;
import com.sw.cmc.adapter.out.persistence.lcd.LiveCodingRepository;
import com.sw.cmc.domain.edt.Editor;
import com.sw.cmc.domain.lcd.LiveCoding;
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
    private final LiveCodingRepository LiveCodingRepository;

    // 특정 LiveCoding에 해당하는 Editor 조회
    public Optional<Editor> getEditorByRoomId(Long roomId) {
        return editorRepository.findByLiveCodingId(roomId);
    }

    // 특정 LiveCoding에 해당하는 Editor 내용 업데이트
    public Editor updateEditorForRoom(Long roomId, Editor editorData) {
        LiveCoding liveCoding = LiveCodingRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        editorData.setLiveCoding(liveCoding);
        return editorRepository.save(editorData);
    }

    // 코드 수정 내용 업데이트
    public Editor updateEditorCode(Long roomId, String newCode) {
        Editor editor = editorRepository.findByLiveCodingId(roomId)
                .orElseThrow(() -> new RuntimeException("Editor not found"));
        editor.setCode(newCode);
        return editorRepository.save(editor);
    }
}
