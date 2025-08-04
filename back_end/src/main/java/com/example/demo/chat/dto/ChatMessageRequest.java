package com.example.demo.chat.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

//    @NotNull(message = "메시지 타입은 필수입니다.")
    private MessageType type;

    @NotNull(message = "채팅방 ID는 필수입니다.")
    @Positive(message = "채팅방 ID는 양수여야 합니다.")
    private Long roomId;

    @NotNull(message = "보낸 사람 ID는 필수입니다.")
    @Positive(message = "보낸 사람 ID는 양수여야 합니다.")
    private Long senderId;

    @NotBlank(message = "메시지 내용은 비어 있을 수 없습니다.")
    private String message;
}