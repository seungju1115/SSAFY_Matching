package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatMessage;
import com.example.demo.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    // 다중 채팅 메시지 처리
    @MessageMapping("/chat.sendMessage")
    public void sendRoomMessage(ChatMessage message) {
        chatService.putMessage(message);
        messagingTemplate.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }

    // 1:1 채팅 메시지 처리
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(ChatMessage message) {
        chatService.putMessage(message);
        messagingTemplate.convertAndSend("/queue/chat/room/" + message.getRoomId(), message);
    }
}
