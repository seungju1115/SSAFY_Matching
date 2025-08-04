package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatRoomMemberRepository;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.ChatRoomMember;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatMemberServiceTest {

    @Mock
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @InjectMocks
    private ChatMemberService chatMemberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createChatRoomMember_shouldCreateMember_whenNotExists() {
        User user = new User();
        ChatRoom chatRoom = new ChatRoom();

        // 중복 멤버 없음으로 설정
        when(chatRoomMemberRepository.existsByUserAndChatRoom(user, chatRoom)).thenReturn(false);

        ChatRoomMember result = chatMemberService.createChatRoomMember(user, chatRoom);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(chatRoom, result.getChatRoom());

        verify(chatRoomMemberRepository, times(1)).existsByUserAndChatRoom(user, chatRoom);
    }

    @Test
    void createChatRoomMember_shouldThrowException_whenMemberExists() {
        User user = new User();
        ChatRoom chatRoom = new ChatRoom();

        // 중복 멤버 있음으로 설정
        when(chatRoomMemberRepository.existsByUserAndChatRoom(user, chatRoom)).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> chatMemberService.createChatRoomMember(user, chatRoom));

        assertEquals(ErrorCode.CHATROOM_MEMBER_ALREADY_EXISTS, exception.getErrorCode());
        verify(chatRoomMemberRepository, times(1)).existsByUserAndChatRoom(user, chatRoom);
    }
}
