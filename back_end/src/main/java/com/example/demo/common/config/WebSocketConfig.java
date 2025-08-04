package com.example.demo.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${front.url}")
    private String frontCORSUrl;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //topic 경로로 메시지 송신 가능
        config.enableSimpleBroker("/topic","/queue");      // 메시지 브로커 경로
        //topic 이 다인 메시지 방 queue가 1:1 메시지 방
        //app 경로로 클라이언트로 부터 수신 가능
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 서버로 메시지를 보낼 prefix
    }

    //1. websocket 핸드셰이크
    //WebSocket 등록용 endpoint
    //해당 위치로 연결을 클라이언트가 시도함
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // SockJS fallback 가능하게 만듦
        registry.addEndpoint("/ws-chat").setAllowedOriginPatterns(frontCORSUrl)  // 프론트 포트 주소(필요시 와일드카드 "*"도 가능하나 권장하지 않음)
                .withSockJS();
    }
}

