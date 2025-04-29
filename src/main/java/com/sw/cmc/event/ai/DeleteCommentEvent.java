package com.sw.cmc.event.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.sw.cmc.event.ai
 * fileName       : DeleteCommentEvent
 * author         : ihw
 * date           : 2025. 4. 30.
 * description    :
 */
@Getter
@AllArgsConstructor
@Builder
public class DeleteCommentEvent {
    private Long reviewId;
}
