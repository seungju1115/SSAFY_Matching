package com.example.demo.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private MessageType type;   // 메시지 타입 (채팅, 입장, 퇴장 등)
    private Long roomId;        // 채팅방 ID
    private Long senderId;      // 보낸 사람 ID
    private String senderName;  // 보낸 사람 이름 (옵션)
    private String Message;     // 메시지 내용
    private String timestamp;   // 보낸 시간 (클라이언트 시간 or 서버시간 문자열)
}