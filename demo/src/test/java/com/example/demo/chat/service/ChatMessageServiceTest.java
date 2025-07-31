package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatMessageRepository;
import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.chat.dto.ChatMessageRequest;
import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.chat.entity.ChatMessage;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatMessageService chatMessageService;

    @Test
    void saveMessage_shouldSaveAndReturnResponse() {
        // given
        Long chatRoomId = 1L;
        Long senderId = 2L;
        String messageText = "Hello, test!";

        ChatMessageRequest request = new ChatMessageRequest(chatRoomId, senderId, messageText);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(chatRoomId);

        User sender = new User();
        sender.setId(senderId);

        ChatMessage savedMessage = new ChatMessage();
        savedMessage.setId(10L);
        savedMessage.setChatRoom(chatRoom);
        savedMessage.setSender(sender);
        savedMessage.setMessage(messageText);
        savedMessage.setCreatedAt(LocalDateTime.now());

        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(savedMessage);

        // when
        ChatMessageResponse response = chatMessageService.saveMessage(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(savedMessage.getId());
        assertThat(response.getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(response.getSenderId()).isEqualTo(senderId);
        assertThat(response.getMessage()).isEqualTo(messageText);
        assertThat(response.getCreatedAt()).isEqualTo(savedMessage.getCreatedAt());

        // verify save was called with expected data
        ArgumentCaptor<ChatMessage> captor = ArgumentCaptor.forClass(ChatMessage.class);
        verify(chatMessageRepository).save(captor.capture());
        ChatMessage captured = captor.getValue();
        assertThat(captured.getChatRoom()).isEqualTo(chatRoom);
        assertThat(captured.getSender()).isEqualTo(sender);
        assertThat(captured.getMessage()).isEqualTo(messageText);
    }

    @Test
    void getAllMessagesByChatRoom_shouldReturnAllMessagesMappedToResponse() {
        // given
        Long chatRoomId = 1L;

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(chatRoomId);

        User sender = new User();
        sender.setId(2L);

        ChatMessage msg1 = new ChatMessage(1L, chatRoom, sender, "msg1", null);
        ChatMessage msg2 = new ChatMessage(2L, chatRoom, sender, "msg2", null);

        when(chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId))
                .thenReturn(List.of(msg1, msg2));

        // when
        List<ChatMessageResponse> responses = chatMessageService.getAllMessagesByChatRoom(chatRoomId);

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(msg1.getId());
        assertThat(responses.get(1).getMessage()).isEqualTo(msg2.getMessage());
    }
}