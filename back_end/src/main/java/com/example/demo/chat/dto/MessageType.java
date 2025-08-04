package com.example.demo.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메시지 타입", enumAsRef = true)
public enum MessageType {
    @Schema(description = "일반 채팅 메시지")
    CHAT,

    @Schema(description = "채팅방 입장 메시지")
    JOIN,

    @Schema(description = "채팅방 퇴장 메시지")
    LEAVE
}