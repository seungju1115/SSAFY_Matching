package com.example.demo.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 메시지 요청 DTO: 클라이언트에서 서버로 보낼 때 사용
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
    private MessageType type;
    private Long roomId;   // 메시지를 보낼 채팅방 ID
    private Long senderId;     // 메시지 보낸 사람 ID
    private String message;    // 메시지 내용
}