package com.sw.cmc.common.config;

import com.sw.cmc.adapter.in.lcd.web.WebSocketAuthInterceptor;
import com.sw.cmc.adapter.in.lcd.web.WebSocketControllerImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * packageName    : com.sw.cmc.common.config
 * fileName       : WebSocketConfig
 * author         : Ko
 * date           : 2025-02-08
 * description    : WebSocket config
 */

@Configuration
@EnableWebSocket // ✅ WebSocket 활성화
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketControllerImpl webSocketHandler;
    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    public WebSocketConfig(WebSocketControllerImpl webSocketHandler, WebSocketAuthInterceptor webSocketAuthInterceptor) {
        this.webSocketHandler = webSocketHandler;
        this.webSocketAuthInterceptor = webSocketAuthInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/livecoding/{roomId}")
                .addInterceptors(webSocketAuthInterceptor) // 🔹 인터셉터 등록
                .setAllowedOriginPatterns("*"); // 모든 Origin 허용
    }

}
