package com.sw.cmc.adapter.in.lcd;

import com.sw.cmc.application.service.EditorService;
import com.sw.cmc.domain.Editor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : com.sw.cmc.adapter.in.web
 * fileName       : EditorController
 * author         : Ko
 * date           : 2025-02-09
 * description    :
 */
@RestController
@RequestMapping("/api/editor")
@RequiredArgsConstructor
public class EditorController {

    private final EditorService editorService;

    // 특정 LiveCodingRoom의 Editor 정보 조회
    @GetMapping("/{roomId}")
    public Editor getEditor(@PathVariable Long roomId) {
        return editorService.getEditorByRoomId(roomId).orElseThrow(() -> new RuntimeException("Editor not found"));
    }

    // 코드 내용 수정 put 수정
//    @PutMapping("/{roomId}")
//    public Editor updateEditorCode(@PathVariable Long roomId, @RequestBody String newCode) {
//        return editorService.updateEditorCode(roomId, newCode);
//    }
}