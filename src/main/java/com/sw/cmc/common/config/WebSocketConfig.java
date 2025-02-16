package com.sw.cmc.common.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * packageName    : com.sw.cmc.common.config
 * fileName       : WebSocketConfig
 * author         : Ko
 * date           : 2025-02-08
 * description    :
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")  // WebSocket 엔드포인트
                .setAllowedOriginPatterns("http://localhost:*")  // CORS 허용 (CorsConfig에서 설정한 대로)
                .withSockJS();  // SockJS로 폴백 처리
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");  // 메시지 브로커 설정 (구독할 경로)
        registry.setApplicationDestinationPrefixes("/app");  // 클라이언트 요청이 이 경로로 시작
    }
}
