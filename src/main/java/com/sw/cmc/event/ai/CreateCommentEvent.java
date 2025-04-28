package com.sw.cmc.event.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.sw.cmc.event.ai
 * fileName       : CreateCommentEvent
 * author         : ihw
 * date           : 2025. 4. 29.
 * description    : 리뷰 생성에 의한 ai 댓글 생성 이벤트 객체
 */
@Getter
@AllArgsConstructor
@Builder
public class CreateCommentEvent {
    private Long reviewId;
    private String title;
    private String content;
    private String codeContent;
    private String codeType;
}
