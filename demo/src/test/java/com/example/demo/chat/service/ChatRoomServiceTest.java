package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.dto.ChatRoomResponse;
import com.example.demo.chat.entity.*;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.entity.Team;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private ChatMemberService chatMemberService;

    private User user1;
    private User user2;
    private Team team;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User();
        user1.setId(1L);

        user2 = new User();
        user2.setId(2L);

        team = new Team();
        team.setId(10L);
    }

    @Test
    void testCreatePrivateChatRoom() {
        ChatRoomRequest request = new ChatRoomRequest();
        request.setUser1Id(1L);
        request.setUser2Id(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(chatMemberService.createChatRoomMember(any(User.class), any(ChatRoom.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    ChatRoom chatRoom = invocation.getArgument(1);
                    ChatRoomMember member = new ChatRoomMember();
                    member.setUser(user);
                    member.setChatRoom(chatRoom);
                    return member;
                });
        when(chatRoomRepository.save(any(ChatRoom.class))).thenAnswer(invocation -> {
            ChatRoom room = invocation.getArgument(0);
            room.setId(100L);
            return room;
        });

        ChatRoomResponse response = chatRoomService.createPrivateChatRoom(request);

        assertNotNull(response);
        assertEquals(RoomType.PRIVATE, response.getRoomType());
        assertEquals(100L, response.getRoomId());
    }

    @Test
    void testCreateTeamChatRoom() {
        ChatRoomRequest request = new ChatRoomRequest();
        request.setTeamId(10L);
        request.setUserId(1L);

        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(userRepository.getReferenceById(1L)).thenReturn(user1);
        when(chatMemberService.createChatRoomMember(any(User.class), any(ChatRoom.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    ChatRoom chatRoom = invocation.getArgument(1);
                    ChatRoomMember member = new ChatRoomMember();
                    member.setUser(user);
                    member.setChatRoom(chatRoom);
                    return member;
                });
        when(chatRoomRepository.save(any(ChatRoom.class))).thenAnswer(invocation -> {
            ChatRoom room = invocation.getArgument(0);
            room.setId(200L);
            return room;
        });

        ChatRoomResponse response = chatRoomService.createTeamChatRoom(request);

        assertNotNull(response);
        assertEquals(RoomType.TEAM, response.getRoomType());
        assertEquals(200L, response.getRoomId());
        assertEquals(10L, response.getTeamId());
    }

    @Test
    void testAddMemberToTeamChatRoom_duplicate() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(1L);
        chatRoom.setRoomType(RoomType.TEAM);

        ChatRoomMember existingMember = new ChatRoomMember();
        existingMember.setUser(user1);
        chatRoom.addMember(existingMember);

        ChatRoomRequest request = new ChatRoomRequest();
        request.setRoomId(1L);
        request.setUserId(1L);

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                chatRoomService.addMemberToTeamChatRoom(request)
        );
        assertEquals("User is already a member of this chat room.", e.getMessage());
    }

    @Test
    void testDeleteChatRoom() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(1L);

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));

        chatRoomService.deleteChatRoom(1L);

        verify(chatRoomRepository, times(1)).delete(chatRoom);
    }
}
