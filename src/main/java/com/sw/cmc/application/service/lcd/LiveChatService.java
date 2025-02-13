package com.sw.cmc.application.service.lcd;

import com.sw.cmc.adapter.out.persistence.lcd.LiveChatRepository;
import com.sw.cmc.adapter.out.persistence.lcd.LiveCodingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.sw.cmc.application.service
 * fileName       : LiveChatService
 * author         : Ko
 * date           : 2025-02-08
 * description    :
 */

@Service
@RequiredArgsConstructor
public class LiveChatService {
    private final LiveChatRepository liveChatRepository;
    private final LiveCodingRepository liveCodingRepository;
}
