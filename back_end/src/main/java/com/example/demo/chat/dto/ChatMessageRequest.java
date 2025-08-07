package com.example.demo.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅 메시지 전송 요청")
public class ChatMessageRequest {

    @Schema(
            description = "메시지 타입",
            example = "CHAT",
            allowableValues = {"CHAT", "JOIN", "LEAVE"},
            required = true
    )
    private MessageType type;

    @Schema(description = "채팅방 ID", example = "1", required = true)
    private Long roomId;

    @Schema(description = "발신자 ID", example = "1", required = true)
    private Long senderId;

    @Schema(description = "메시지 내용", example = "안녕하세요!", required = true)
    private String message;
}