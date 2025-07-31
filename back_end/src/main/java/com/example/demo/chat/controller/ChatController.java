package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatMessageRequest;
import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.chat.service.ChatMessageService;
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
    private final ChatMessageService chatMessageService;
    // 다중 채팅 메시지 처리
    @MessageMapping("/chat.sendMessage")
    public void sendRoomMessage(ChatMessageRequest socketMessage) {
        ChatMessageResponse savedMessage = chatMessageService.saveMessage(socketMessage);
        messagingTemplate.convertAndSend("/topic/chat/room/" + savedMessage.getChatRoomId(), savedMessage);
    }

    // 1:1 채팅 메시지 처리
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(ChatMessageRequest socketMessage) {
        ChatMessageResponse savedMessage = chatMessageService.saveMessage(socketMessage);
        messagingTemplate.convertAndSend("/queue/chat/room/" + savedMessage.getChatRoomId(), savedMessage);
    }
}
