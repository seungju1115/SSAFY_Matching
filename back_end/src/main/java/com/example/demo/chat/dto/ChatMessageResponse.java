package com.example.demo.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

// 메시지 응답 DTO: 서버에서 클라이언트로 보낼 때 사용
@Getter
@AllArgsConstructor
public class ChatMessageResponse {
    private Long id;               // 메시지 고유 ID
    private Long chatRoomId;       // 채팅방 ID
    private Long senderId;         // 보낸 사람 ID
    private String message;        // 메시지 내용
    private LocalDateTime createdAt;  // 메시지 생성 시각
}
