package com.sw.cmc.domain.editor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.domain.editor
 * fileName       : EditorDomain
 * author         : An Seung Gi
 * date           : 2025-02-25
 * description    : Editor Domain Object
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditorDomain {
    private Long codeEditNum;
    private String content;
    private String language;
    private Long userNum;
    private String createdAt;
    private String updatedAt;
}
