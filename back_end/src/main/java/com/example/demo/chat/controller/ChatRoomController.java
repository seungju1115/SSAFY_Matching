package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.dto.ChatRoomResponse;
import com.example.demo.chat.service.ChatMessageService;
import com.example.demo.chat.service.ChatRoomService;
import com.example.demo.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    // 채팅방 입장 시 해당 방의 모든 메시지 로그 조회
    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChatMessages(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(ApiResponse.ok(chatMessageService.getAllMessagesByChatRoom(chatRoomId)));
    }

    @PostMapping("/private")
    public ResponseEntity<ApiResponse<ChatRoomResponse>> createPrivateChatRoom(@RequestBody ChatRoomRequest chatRoomRequest) {
        return ResponseEntity.ok(ApiResponse.created(chatRoomService.createPrivateChatRoom(chatRoomRequest)));
    }
}
