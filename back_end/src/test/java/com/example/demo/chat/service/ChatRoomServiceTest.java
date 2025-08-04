package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.dto.ChatRoomResponse;
import com.example.demo.chat.entity.*;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.entity.Team;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ChatMemberService chatMemberService;

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Test
    void createPrivateChatRoom_existingRoom_returnsExistingRoom() {
        // Arrange
        Long user1Id = 1L;
        Long user2Id = 2L;

        ChatRoom existingRoom = new ChatRoom();
        existingRoom.setId(100L);
        existingRoom.setRoomType(RoomType.PRIVATE);

        User user1 = new User();
        ChatRoomMember crm1 = new ChatRoomMember();
        crm1.setUser(user1);
        crm1.setChatRoom(existingRoom);  // 여기가 null 이면 NPE 발생
        List<ChatRoomMember> members1 = new ArrayList<>();
        members1.add(crm1);
        user1.setChatRoomMembers(members1);

        User user2 = new User();
        ChatRoomMember crm2 = new ChatRoomMember();
        crm2.setUser(user2);
        crm2.setChatRoom(existingRoom);
        List<ChatRoomMember> members2 = new ArrayList<>();
        members2.add(crm2);
        user2.setChatRoomMembers(members2);

        when(userRepository.findByIdWithChatRoomMembers(user1Id)).thenReturn(Optional.of(user1));
        when(userRepository.findByIdWithChatRoomMembers(user2Id)).thenReturn(Optional.of(user2));

        ChatRoomRequest request = new ChatRoomRequest();
        request.setUser1Id(user1Id);
        request.setUser2Id(user2Id);

        // Act
        ChatRoomResponse response = chatRoomService.createPrivateChatRoom(request);

        // Assert
        assertNotNull(response);
        assertEquals(existingRoom.getId(), response.getRoomId());
        assertEquals(RoomType.PRIVATE, response.getRoomType());

        verify(chatRoomRepository, never()).save(any());
        verify(chatMemberService, never()).createChatRoomMember(any(), any());
    }

    @Test
    void createPrivateChatRoom_noExistingRoom_createsNewRoom() {
        // Arrange
        Long user1Id = 1L;
        Long user2Id = 2L;

        User user1 = new User();
        user1.setChatRoomMembers(new ArrayList<>());

        User user2 = new User();
        user2.setChatRoomMembers(new ArrayList<>());

        when(userRepository.findByIdWithChatRoomMembers(user1Id)).thenReturn(Optional.of(user1));
        when(userRepository.findByIdWithChatRoomMembers(user2Id)).thenReturn(Optional.of(user2));

        ChatRoomRequest request = new ChatRoomRequest();
        request.setRoomType(RoomType.PRIVATE);
        request.setUser1Id(user1Id);
        request.setUser2Id(user2Id);

        when(chatMemberService.createChatRoomMember(eq(user1), any(ChatRoom.class)))
                .thenAnswer(invocation -> {
                    ChatRoom chatRoom = invocation.getArgument(1);
                    ChatRoomMember member = new ChatRoomMember();
                    member.setUser(user1);
                    member.setChatRoom(chatRoom);
                    return member;
                });

        ChatRoom savedRoom = new ChatRoom();
        savedRoom.setId(101L);
        savedRoom.setRoomType(RoomType.PRIVATE);

        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(savedRoom);

        // Act
        ChatRoomResponse response = chatRoomService.createPrivateChatRoom(request);

        // Assert
        assertNotNull(response);
        assertEquals(101L, response.getRoomId());
        assertEquals(RoomType.PRIVATE, response.getRoomType());

        verify(chatRoomRepository).save(any(ChatRoom.class));
        verify(chatMemberService, times(2)).createChatRoomMember(any(User.class), any(ChatRoom.class));
    }

    @Test
    void createTeamChatRoom_success() {
        // Arrange
        Long teamId = 10L;
        Long userId = 20L;

        Team team = new Team();
        team.setId(teamId);

        User user = new User();
        user.setId(userId);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ChatRoomMember member = new ChatRoomMember();
        when(chatMemberService.createChatRoomMember(eq(user), any(ChatRoom.class))).thenReturn(member);

        ChatRoom savedRoom = new ChatRoom();
        savedRoom.setId(30L);
        savedRoom.setRoomType(RoomType.TEAM);
        savedRoom.setTeam(team);

        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(savedRoom);

        ChatRoomRequest request = new ChatRoomRequest();
        request.setTeamId(teamId);
        request.setUserId(userId);

        // Act
        ChatRoomResponse response = chatRoomService.createTeamChatRoom(request);

        // Assert
        assertNotNull(response);
        assertEquals(30L, response.getRoomId());
        assertEquals(RoomType.TEAM, response.getRoomType());
        assertEquals(teamId, response.getTeamId());

        verify(chatRoomRepository).save(any(ChatRoom.class));
        verify(chatMemberService).createChatRoomMember(eq(user), any(ChatRoom.class));
    }

    @Test
    void addMemberToTeamChatRoom_success() {
        // Arrange
        Long chatRoomId = 50L;
        Long userId = 60L;

        Team team = new Team();
        team.setId(1L);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(chatRoomId);
        chatRoom.setRoomType(RoomType.TEAM);
        chatRoom.setTeam(team);
        chatRoom.setMembers(new ArrayList<>());  // 멤버 없음

        User user = new User();
        user.setId(userId);

        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ChatRoomMember newMember = new ChatRoomMember();
        when(chatMemberService.createChatRoomMember(eq(user), eq(chatRoom))).thenReturn(newMember);

        // Act
        chatRoomService.addMemberToTeamChatRoom(new ChatRoomRequest() {{
            setRoomId(chatRoomId);
            setUserId(userId);
        }});

        // Assert
        verify(chatRoomRepository).save(chatRoom);
        verify(chatMemberService).createChatRoomMember(eq(user), eq(chatRoom));
    }

    @Test
    void addMemberToTeamChatRoom_throwsIfNotTeamRoom() {
        // Arrange
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomType(RoomType.PRIVATE);

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));

        ChatRoomRequest request = new ChatRoomRequest();
        request.setRoomId(1L);
        request.setUserId(10L);

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class,
                () -> chatRoomService.addMemberToTeamChatRoom(request));
        assertEquals(ErrorCode.INVALID_CHAT_ROOM_TYPE, ex.getErrorCode());
    }

    @Test
    void addMemberToTeamChatRoom_throwsIfMemberAlreadyExists() {
        // Arrange
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);

        ChatRoomMember existingMember = new ChatRoomMember();
        existingMember.setUser(existingUser);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomType(RoomType.TEAM);
        chatRoom.setMembers(new ArrayList<>());
        chatRoom.getMembers().add(existingMember);  // 기존 멤버 추가

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(chatRoom));
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));  // 반드시 필요

        ChatRoomRequest request = new ChatRoomRequest();
        request.setRoomId(1L);
        request.setUserId(userId);

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class,
                () -> chatRoomService.addMemberToTeamChatRoom(request));
        assertEquals(ErrorCode.CHATROOM_MEMBER_ALREADY_EXISTS, ex.getErrorCode());
    }


    @Test
    void deleteChatRoom_success() {
        // Arrange
        Long chatRoomId = 99L;
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(chatRoomId);

        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));

        // Act
        chatRoomService.deleteChatRoom(chatRoomId);

        // Assert
        verify(chatRoomRepository).delete(chatRoom);
    }

    @Test
    void deleteChatRoom_throwsIfNotFound() {
        // Arrange
        Long chatRoomId = 99L;
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class,
                () -> chatRoomService.deleteChatRoom(chatRoomId));
        assertEquals(ErrorCode.CHAT_ROOM_NOT_FOUND, ex.getErrorCode());
    }
}
