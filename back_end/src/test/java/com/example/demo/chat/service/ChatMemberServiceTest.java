package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatRoomMemberRepository;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.ChatRoomMember;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatMemberServiceTest {

    @Mock
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @InjectMocks
    private ChatMemberService chatMemberService;

    private User user;
    private ChatRoom chatRoom;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        chatRoom = new ChatRoom();
    }

    @Test
    @DisplayName("채팅 룸 맴버 중간 테이블 생성 성공")
    void createChatRoomMember_shouldCreateMember_whenNotExists() {
        // 중복 멤버 없음으로 설정
        when(chatRoomMemberRepository.existsByUserAndChatRoom(user, chatRoom)).thenReturn(false);

        ChatRoomMember result = chatMemberService.createChatRoomMember(user, chatRoom);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(chatRoom, result.getChatRoom());

        verify(chatRoomMemberRepository, times(1)).existsByUserAndChatRoom(user, chatRoom);
    }

    @Test
    @DisplayName("채팅 룸 맴버 중간 테이블 생성 실패 - 이미 등록된 맴버")
    void createChatRoomMember_shouldThrowException_whenMemberExists() {
        // 중복 멤버 있음으로 설정
        when(chatRoomMemberRepository.existsByUserAndChatRoom(user, chatRoom)).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> chatMemberService.createChatRoomMember(user, chatRoom));

        assertEquals(ErrorCode.CHATROOM_MEMBER_ALREADY_EXISTS, exception.getErrorCode());
        verify(chatRoomMemberRepository, times(1)).existsByUserAndChatRoom(user, chatRoom);
    }
}
