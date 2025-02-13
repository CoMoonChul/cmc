package com.sw.cmc.domain.lcd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.sw.cmc.domain.lcd
 * fileName       : webSocketMessage
 * author         : 82104
 * date           : 2025-02-11
 * description    :
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class webSocketMessage {
    private String content; // 메시지 내용
    private String sender;  // 보낸 사람
}
