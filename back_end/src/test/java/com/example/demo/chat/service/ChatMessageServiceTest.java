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
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    private ChatRoom chatRoom;
    private User user;
    private ChatMessage chatMessage1,chatMessage2,savedMessage;
    private ChatMessageRequest chatMessageRequest;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatRoom = new ChatRoom();
        chatRoom.setId(1L);

        user = new User();
        user.setId(2L);

        chatMessage1 = new ChatMessage();
        chatMessage1.setId(10L);
        chatMessage1.setChatRoom(chatRoom);
        chatMessage1.setSender(user);
        chatMessage1.setMessage("Hello");
        chatMessage1.setCreatedAt(LocalDateTime.now());

        chatMessage2 = new ChatMessage();
        chatMessage2.setId(11L);
        chatMessage2.setChatRoom(chatRoom);
        chatMessage2.setSender(user);
        chatMessage2.setMessage("msg2");
        chatMessage2.setCreatedAt(LocalDateTime.now());

        savedMessage = new ChatMessage();
        savedMessage.setId(10L);
        savedMessage.setChatRoom(chatRoom);
        savedMessage.setSender(user);
        savedMessage.setMessage("Hello");
        savedMessage.setCreatedAt(LocalDateTime.now());

        chatMessageRequest = new ChatMessageRequest();
        chatMessageRequest.setRoomId(1L);
        chatMessageRequest.setSenderId(10L);
        chatMessageRequest.setMessage("Hello");
    }

    @Test
    @DisplayName("채팅 저장 성공")
    void saveMessage_success() {
        when(chatRoomRepository.findById(chatMessageRequest.getRoomId())).thenReturn(Optional.of(chatRoom));
        when(userRepository.findById(chatMessageRequest.getSenderId())).thenReturn(Optional.of(user));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(savedMessage);

        // 실행
        ChatMessageResponse response = chatMessageService.saveMessage(chatMessageRequest);

        // 검증
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals(1L, response.getChatRoomId());
        assertEquals(2L, response.getSenderId());
        assertEquals("Hello", response.getMessage());
        assertNotNull(response.getCreatedAt());

        verify(chatRoomRepository).findById(chatMessageRequest.getRoomId());
        verify(userRepository).findById(chatMessageRequest.getSenderId());
        verify(chatMessageRepository).save(any(ChatMessage.class));
    }

    @Test
    @DisplayName("채팅 저장 실패 - 채팅 룸 없음")
    void saveMessage_chatRoomNotFound() {
        when(chatRoomRepository.findById(chatRoom.getId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> chatMessageService.saveMessage(chatMessageRequest));

        assertEquals(ErrorCode.CHAT_ROOM_NOT_FOUND, exception.getErrorCode());
        verify(chatRoomRepository).findById(chatRoom.getId());
        verify(userRepository, never()).findById(any());
        verify(chatMessageRepository, never()).save(any());
    }

    @Test
    @DisplayName("채팅 저장 실패 - 유저 없음")
    void saveMessage_senderNotFound() {
        when(chatRoomRepository.findById(chatRoom.getId())).thenReturn(Optional.of(chatRoom));
        when(userRepository.findById(chatMessageRequest.getSenderId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> chatMessageService.saveMessage(chatMessageRequest));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(chatRoomRepository).findById(chatRoom.getId());
        verify(userRepository).findById(chatMessageRequest.getSenderId());
        verify(chatMessageRepository, never()).save(any());
    }

    @Test
    @DisplayName("채팅방 채팅 로그 조회")
    void getAllMessagesByChatRoom_success() {
        when(chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(1L))
                .thenReturn(List.of(chatMessage1, chatMessage2));

        List<ChatMessageResponse> responses = chatMessageService.getAllMessagesByChatRoom(1L);

        assertEquals(2, responses.size());
        assertEquals(chatMessage1.getMessage(), responses.get(0).getMessage());
        assertEquals(chatMessage2.getMessage(), responses.get(1).getMessage());

        verify(chatMessageRepository).findByChatRoomIdOrderByCreatedAtAsc(1L);
    }
}
