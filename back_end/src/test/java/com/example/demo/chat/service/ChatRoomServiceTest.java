package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.dto.ChatRoomResponse;
import com.example.demo.chat.entity.*;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.entity.Team;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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
        user1.setChatRoomMembers(new ArrayList<>());

        user2 = new User();
        user2.setId(2L);
        user2.setChatRoomMembers(new ArrayList<>());

        team = new Team();
        team.setId(10L);
    }

    @Test
    void createPrivateChatRoom_newRoomCreated() {
        ChatRoomRequest request = new ChatRoomRequest();
        request.setUser1Id(1L);
        request.setUser2Id(2L);

        when(userRepository.findByIdWithChatRoomMembers(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findByIdWithChatRoomMembers(2L)).thenReturn(Optional.of(user2));

        when(chatMemberService.createChatRoomMember(any(User.class), any(ChatRoom.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    ChatRoom room = invocation.getArgument(1);
                    ChatRoomMember member = new ChatRoomMember();
                    member.setUser(user);
                    member.setChatRoom(room);
                    return member;
                });

        when(chatRoomRepository.save(any(ChatRoom.class))).thenAnswer(invocation -> {
            ChatRoom room = invocation.getArgument(0);
            room.setId(100L);
            return room;
        });

        ChatRoomResponse response = chatRoomService.createPrivateChatRoom(request);

        assertNotNull(response);
        assertEquals(100L, response.getRoomId());
        assertEquals(RoomType.PRIVATE, response.getRoomType());

        // 멤버가 user1, user2인지 확인
        verify(chatMemberService, times(2)).createChatRoomMember(any(User.class), any(ChatRoom.class));
        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
    }

    @Test
    void createPrivateChatRoom_existingRoomReturned() {
        ChatRoomRequest request = new ChatRoomRequest();
        request.setUser1Id(1L);
        request.setUser2Id(2L);

        ChatRoom existingRoom = new ChatRoom();
        existingRoom.setId(200L);
        existingRoom.setRoomType(RoomType.PRIVATE);

        // user1이 속한 방 세팅
        ChatRoomMember member1 = new ChatRoomMember();
        member1.setChatRoom(existingRoom);
        member1.setUser(user1);
        user1.getChatRoomMembers().add(member1);

        // user2가 속한 방 세팅 (동일 방)
        ChatRoomMember member2 = new ChatRoomMember();
        member2.setChatRoom(existingRoom);
        member2.setUser(user2);
        user2.getChatRoomMembers().add(member2);

        when(userRepository.findByIdWithChatRoomMembers(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findByIdWithChatRoomMembers(2L)).thenReturn(Optional.of(user2));

        ChatRoomResponse response = chatRoomService.createPrivateChatRoom(request);

        assertNotNull(response);
        assertEquals(200L, response.getRoomId());
        assertEquals(RoomType.PRIVATE, response.getRoomType());

        // 새로 생성하지 않음
        verify(chatRoomRepository, never()).save(any(ChatRoom.class));
        verify(chatMemberService, never()).createChatRoomMember(any(User.class), any(ChatRoom.class));
    }

    @Test
    void createPrivateChatRoom_user1NotFound_throwsException() {
        ChatRoomRequest request = new ChatRoomRequest();
        request.setUser1Id(1L);
        request.setUser2Id(2L);

        when(userRepository.findByIdWithChatRoomMembers(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                chatRoomService.createPrivateChatRoom(request)
        );
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void createPrivateChatRoom_user2NotFound_throwsException() {
        ChatRoomRequest request = new ChatRoomRequest();
        request.setUser1Id(1L);
        request.setUser2Id(2L);

        when(userRepository.findByIdWithChatRoomMembers(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findByIdWithChatRoomMembers(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                chatRoomService.createPrivateChatRoom(request)
        );
        assertEquals("User not found", exception.getMessage());
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
