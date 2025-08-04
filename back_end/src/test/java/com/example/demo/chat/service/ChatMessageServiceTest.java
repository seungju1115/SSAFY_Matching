package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatMessageRepository;
import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.chat.dto.ChatMessageRequest;
import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.chat.entity.ChatMessage;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatMessageService chatMessageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveMessage_success() {
        // 준비
        ChatMessageRequest request = new ChatMessageRequest();
        request.setRoomId(1L);
        request.setSenderId(2L);
        request.setMessage("Hello");

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(1L);

        User sender = new User();
        sender.setId(2L);

        ChatMessage savedMessage = new ChatMessage();
        savedMessage.setId(10L);
        savedMessage.setChatRoom(chatRoom);
        savedMessage.setSender(sender);
        savedMessage.setMessage("Hello");
        savedMessage.setCreatedAt(LocalDateTime.now());

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        when(userRepository.findById(2L)).thenReturn(Optional.of(sender));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(savedMessage);

        // 실행
        ChatMessageResponse response = chatMessageService.saveMessage(request);

        // 검증
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals(1L, response.getChatRoomId());
        assertEquals(2L, response.getSenderId());
        assertEquals("Hello", response.getMessage());
        assertNotNull(response.getCreatedAt());

        verify(chatRoomRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(chatMessageRepository).save(any(ChatMessage.class));
    }

    @Test
    void saveMessage_chatRoomNotFound() {
        ChatMessageRequest request = new ChatMessageRequest();
        request.setRoomId(1L);
        request.setSenderId(2L);
        request.setMessage("Hello");

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> chatMessageService.saveMessage(request));

        assertEquals(ErrorCode.CHAT_ROOM_NOT_FOUND, exception.getErrorCode());
        verify(chatRoomRepository).findById(1L);
        verify(userRepository, never()).findById(any());
        verify(chatMessageRepository, never()).save(any());
    }

    @Test
    void saveMessage_senderNotFound() {
        ChatMessageRequest request = new ChatMessageRequest();
        request.setRoomId(1L);
        request.setSenderId(2L);
        request.setMessage("Hello");

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(1L);

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> chatMessageService.saveMessage(request));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(chatRoomRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(chatMessageRepository, never()).save(any());
    }

    @Test
    void getAllMessagesByChatRoom_success() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(1L);

        User sender = new User();
        sender.setId(2L);

        ChatMessage message1 = new ChatMessage();
        message1.setId(10L);
        message1.setChatRoom(chatRoom);
        message1.setSender(sender);
        message1.setMessage("msg1");
        message1.setCreatedAt(LocalDateTime.now());

        ChatMessage message2 = new ChatMessage();
        message2.setId(11L);
        message2.setChatRoom(chatRoom);
        message2.setSender(sender);
        message2.setMessage("msg2");
        message2.setCreatedAt(LocalDateTime.now());

        when(chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(1L))
                .thenReturn(List.of(message1, message2));

        List<ChatMessageResponse> responses = chatMessageService.getAllMessagesByChatRoom(1L);

        assertEquals(2, responses.size());
        assertEquals("msg1", responses.get(0).getMessage());
        assertEquals("msg2", responses.get(1).getMessage());

        verify(chatMessageRepository).findByChatRoomIdOrderByCreatedAtAsc(1L);
    }
}
