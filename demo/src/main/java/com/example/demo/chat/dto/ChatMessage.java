package com.example.demo.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private boolean type;
    private String message;
    private String sender;
    private String roomId;   // 추가
    private String dateTime;
}
