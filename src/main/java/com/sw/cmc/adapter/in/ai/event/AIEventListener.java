package com.sw.cmc.adapter.in.ai.event;

import com.sw.cmc.application.port.in.ai.AIUseCase;
import com.sw.cmc.domain.ai.AIDomain;
import com.sw.cmc.event.ai.CreateCommentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * packageName    : com.sw.cmc.adapter.in.ai.event
 * fileName       : AIEventListener
 * author         : ihw
 * date           : 2025. 4. 29.
 * description    : ai 이벤트 리스너
 */
@Component
@RequiredArgsConstructor
public class AIEventListener {

    private final AIUseCase aiUseCase;

    @EventListener
    public void handleCreateComment(CreateCommentEvent event) throws IOException {
        AIDomain aiDomain = AIDomain.builder()
                .reviewId(event.getReviewId())
                .title(event.getTitle())
                .content(event.getContent())
                .codeContent(event.getCodeContent())
                .codeType(event.getCodeType())
                .build();

        aiUseCase.createComment(aiDomain);
    }

    @EventListener
    public void handleDeleteComment(CreateCommentEvent event) {
        AIDomain aiDomain = AIDomain.builder()
                .reviewId(event.getReviewId())
                .build();

        aiUseCase.deleteComment(aiDomain);
    }
}
