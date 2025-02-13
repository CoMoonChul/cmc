package com.sw.cmc.application.service.live_code;

import com.sw.cmc.adapter.out.persistence.repositroy.LiveChatRepository;
import com.sw.cmc.adapter.out.persistence.repositroy.LiveCodingRoomRepository;
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
    private final LiveCodingRoomRepository liveCodingRoomRepository;
}
