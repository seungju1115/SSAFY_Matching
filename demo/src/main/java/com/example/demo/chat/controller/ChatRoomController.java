package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatMessageService chatMessageService;

    // 채팅방 입장 시 해당 방의 모든 메시지 로그 조회
    @GetMapping("/{chatRoomId}/messages")
    public List<ChatMessageResponse> getChatMessages(@PathVariable Long chatRoomId) {
        return chatMessageService.getAllMessagesByChatRoom(chatRoomId);
    }
}
