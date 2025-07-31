package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatMessageRequest;
import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.chat.dto.MessageType;
import com.example.demo.chat.service.ChatMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChatControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private ChatMessageService chatMessageService;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendRoomMessage_shouldSaveAndSendMessage() {
        // given
        ChatMessageRequest socketMessage = new ChatMessageRequest(
                MessageType.CHAT,  // MessageType 타입으로 첫번째 인자
                1L,                // roomId
                2L,                // senderId
                "Hello"            // Message (대문자 M 주의)
        ); // roomId, senderId, message 등 채움
        ChatMessageResponse savedResponse = new ChatMessageResponse(10L, 1L, 2L, "Hello", null);

        when(chatMessageService.saveMessage(any(ChatMessageRequest.class))).thenReturn(savedResponse);

        // when
        chatController.sendRoomMessage(socketMessage);

        // then
        verify(chatMessageService, times(1)).saveMessage(any(ChatMessageRequest.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/chat/room/" + savedResponse.getChatRoomId()), eq(savedResponse));
    }

    @Test
    void sendPrivateMessage_shouldSaveAndSendMessage() {
        // given
        ChatMessageRequest socketMessage = new ChatMessageRequest(
                MessageType.CHAT,  // MessageType 타입으로 첫번째 인자
                1L,                // roomId
                2L,                // senderId
                "Hello"            // Message (대문자 M 주의)
        );
        ChatMessageResponse savedResponse = new ChatMessageResponse(20L, 1L, 2L, "Private Hello", null);

        when(chatMessageService.saveMessage(any(ChatMessageRequest.class))).thenReturn(savedResponse);

        // when
        chatController.sendPrivateMessage(socketMessage);

        // then
        verify(chatMessageService, times(1)).saveMessage(any(ChatMessageRequest.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/queue/chat/room/" + savedResponse.getChatRoomId()), eq(savedResponse));
    }
}