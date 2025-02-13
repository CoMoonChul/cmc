package com.sw.cmc.adapter.in.lcd;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in.lcd
 * fileName       : WebSocketController
 * author         : 82104
 * date           : 2025-02-11
 * description    :
 */

@RestController
public class WebSocketControllerImpl {

    @MessageMapping("/chat") // 클라이언트가 /app/chat 으로 메시지를 보낼 때 실행됨
    @SendTo("/topic/messages") // 모든 구독자에게 메시지를 전송
    public String sendMessage(String message) {
        System.out.println("##2222 s📩 받은 메시지: " + message);
        return message;
    }
}
