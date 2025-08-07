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

    @NotNull(message = "채팅방 ID는 필수입니다.")
    @Positive(message = "채팅방 ID는 양수여야 합니다.")
    @Schema(description = "채팅방 ID", example = "1", required = true)
    private Long roomId;

    @NotNull(message = "보낸 사람 ID는 필수입니다.")
    @Positive(message = "보낸 사람 ID는 양수여야 합니다.")
    @Schema(description = "발신자 ID", example = "1", required = true)
    private Long senderId;

    @NotBlank(message = "메시지 내용은 비어 있을 수 없습니다.")
    @Schema(description = "메시지 내용", example = "안녕하세요!", required = true)
    private String message;
}