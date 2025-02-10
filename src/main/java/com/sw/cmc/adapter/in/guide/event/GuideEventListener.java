package com.sw.cmc.adapter.in.guide.event;

import com.sw.cmc.application.port.in.guide.GuideUseCase;
import com.sw.cmc.domain.guide.GuideDomain;
import com.sw.cmc.event.user.TempLoginEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.sw.cmc.adapter.in.guide.event
 * fileName       : GuideEventListener
 * author         : ihw
 * date           : 2025. 2. 10.
 * description    :
 */
@Component
@RequiredArgsConstructor
public class GuideEventListener {
    private final GuideUseCase guideUseCase;

    @EventListener
    public void handleTempLogin(TempLoginEvent tempLoginEvent) throws Exception {
        GuideDomain guideDomain = new GuideDomain(3L, "wow");
        guideUseCase.getGuideDTO(guideDomain);
    }
}