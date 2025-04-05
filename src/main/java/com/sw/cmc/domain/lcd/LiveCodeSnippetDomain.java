package com.sw.cmc.domain.lcd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * packageName    : com.sw.cmc.domain.live_coding.model
 * fileName       : LiveCodeSnippetDomain
 * author         : Ko
 * date           : 2025-04-05
 * description    : 라이브 코드 도메인 객체
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveCodeSnippetDomain implements Serializable {
    private UUID roomId;              // 방 ID
    private String code;              // 전체 코드
    private Diff diff;                // 변경된 코드 영역 (Diff 정보)
    private String language;          // 언어 (예: java, javascript 등)
    private LocalDateTime lastModified; // 마지막 수정 시간
    private CursorPosition cursorPos; // 커서 위치 정보
    private Long userNum;             // 작성자 (옵션 - 추가함)

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Diff implements Serializable {
        private int start;   // 시작 위치
        private int length;  // 변경 전 길이
        private String text; // 변경된 텍스트
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CursorPosition implements Serializable {
        private int line;  // 커서 줄 위치 (0-based)
        private int ch;    // 해당 줄의 문자 위치 (0-based)
    }
}
