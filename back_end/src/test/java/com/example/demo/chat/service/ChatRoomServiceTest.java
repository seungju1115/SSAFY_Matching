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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    private User user1,user2;
    private ChatRoomMember chatRoomMember1,chatRoomMember2;
    private ChatRoom chatRoom;
    private ChatRoomRequest chatRoomRequest;
    private Team team;
    @BeforeEach
    void setUp(){
        chatRoom = new ChatRoom();
        chatRoom.setId(100L);
        chatRoom.setRoomType(RoomType.PRIVATE);

        user1 = new User();
        user1.setId(1L);
        chatRoomMember1 = new ChatRoomMember();
        chatRoomMember1.setUser(user1);
        chatRoomMember1.setChatRoom(chatRoom);

        user2 = new User();
        user2.setId(2L);
        chatRoomMember2 = new ChatRoomMember();
        chatRoomMember2.setUser(user2);
        chatRoomMember2.setChatRoom(chatRoom);

        team = new Team();
        team.setId(1L);

        chatRoomRequest = new ChatRoomRequest();
        chatRoomRequest.setRoomId(chatRoom.getId());
        chatRoomRequest.setTeamId(team.getId());
        chatRoomRequest.setUserId(team.getId());
        chatRoomRequest.setUser1Id(user1.getId());
        chatRoomRequest.setUser2Id(user2.getId());
    }

    @Test
    @DisplayName("1:1 채팅방 생성 - 이미 존재하는 채팅 방")
    void createPrivateChatRoom_existingRoom_returnsExistingRoom() {
        when(userRepository.findByIdWithChatRoomMembers(chatRoomRequest.getUser1Id())).thenReturn(Optional.of(user1));
        when(userRepository.findByIdWithChatRoomMembers(chatRoomRequest.getUser2Id())).thenReturn(Optional.of(user2));

        ChatRoomResponse response = chatRoomService.createPrivateChatRoom(chatRoomRequest);

        // Assert
        assertNotNull(response);
        assertEquals(chatRoom.getId(), response.getRoomId());
        assertEquals(RoomType.PRIVATE, response.getRoomType());

        verify(chatRoomRepository, never()).save(any());
        verify(chatMemberService, never()).createChatRoomMember(any(), any());
    }

    @Test
    @DisplayName("1:1 채팅방 생성 성공")
    void createPrivateChatRoom_noExistingRoom_createsNewRoom() {
        chatRoomMember1.setUser(null);
        chatRoomMember1.setChatRoom(null);
        chatRoomMember2.setUser(null);
        chatRoomMember2.setChatRoom(null);
        when(userRepository.findByIdWithChatRoomMembers(chatRoomRequest.getUser1Id())).thenReturn(Optional.of(user1));
        when(userRepository.findByIdWithChatRoomMembers(chatRoomRequest.getUser2Id())).thenReturn(Optional.of(user2));

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
        ChatRoomResponse response = chatRoomService.createPrivateChatRoom(chatRoomRequest);

        // Assert
        assertNotNull(response);
        assertEquals(101L, response.getRoomId());
        assertEquals(RoomType.PRIVATE, response.getRoomType());

        verify(chatRoomRepository).save(any(ChatRoom.class));
        verify(chatMemberService, times(2)).createChatRoomMember(any(User.class), any(ChatRoom.class));
    }

    @Test
    @DisplayName("1:1 채팅방 생성 실패 - 유저 id 없음")
    void createPrivateChatRoom_failWithUserNotFound() {
        when(userRepository.findByIdWithChatRoomMembers(chatRoomRequest.getUser1Id())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class,
                () -> chatRoomService.createPrivateChatRoom(chatRoomRequest));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(chatRoomRepository, never()).save(any());
    }

    @Test
    @DisplayName("팀 채팅방 생성 성공")
    void createTeamChatRoom_success() {
        when(teamRepository.findById(chatRoomRequest.getTeamId())).thenReturn(Optional.of(team));
        when(userRepository.findById(chatRoomRequest.getUserId())).thenReturn(Optional.of(user1));

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

        ChatRoomResponse response = chatRoomService.createTeamChatRoom(chatRoomRequest);

        // Assert
        assertNotNull(response);
        assertEquals(101L, response.getRoomId());
        assertEquals(RoomType.PRIVATE, response.getRoomType());

        verify(chatRoomRepository).save(any(ChatRoom.class));
        verify(chatMemberService, times(1)).createChatRoomMember(any(User.class), any(ChatRoom.class));
    }

    @Test
    @DisplayName("팀 채팅방 생성 실패 - 유저 id 없음")
    void createTeamChatRoom_failWithUserNotFound() {
        when(teamRepository.findById(chatRoomRequest.getTeamId())).thenReturn(Optional.of(team));
        when(userRepository.findById(chatRoomRequest.getUserId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> chatRoomService.createTeamChatRoom(chatRoomRequest));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(chatRoomRepository, never()).save(any());
    }

    @Test
    @DisplayName("팀 채팅방 생성 실패 - 팀 id 없음")
    void createTeamChatRoom_failWithTeamNotFound() {
        when(teamRepository.findById(chatRoomRequest.getTeamId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> chatRoomService.createTeamChatRoom(chatRoomRequest));

        assertEquals(ErrorCode.TEAM_NOT_FOUND, exception.getErrorCode());
        verify(chatRoomRepository, never()).save(any());
    }

    @Test
    @DisplayName("팀 채팅에 멤버 추가 성공")
    void addMemberToTeamChatRoom_success() {
        chatRoom.setRoomType(RoomType.TEAM);
        chatRoom.setTeam(team);
        chatRoomMember1.setUser(null);
        chatRoomMember1.setChatRoom(null);
        when(chatRoomRepository.findById(chatRoomRequest.getRoomId())).thenReturn(Optional.of(chatRoom));
        when(userRepository.findById(chatRoomRequest.getUserId())).thenReturn(Optional.of(user1));

        ChatRoomMember newMember = new ChatRoomMember();
        when(chatMemberService.createChatRoomMember(eq(user1), eq(chatRoom))).thenReturn(newMember);

        // Act
        chatRoomService.addMemberToTeamChatRoom(new ChatRoomRequest() {{
            setRoomId(chatRoom.getId());
            setUserId(user1.getId());
        }});

        // Assert
        verify(chatRoomRepository).save(chatRoom);
        verify(chatMemberService).createChatRoomMember(eq(user1), eq(chatRoom));
    }

    @Test
    @DisplayName("팀 멤버 추가 실패 - 팀채팅방이 아님")
    void addMemberToTeamChatRoom_throwsIfNotTeamRoom() {
        chatRoom.setRoomType(RoomType.PRIVATE);
        when(chatRoomRepository.findById(chatRoomRequest.getRoomId())).thenReturn(Optional.of(chatRoom));
        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class,
                () -> chatRoomService.addMemberToTeamChatRoom(chatRoomRequest));
        assertEquals(ErrorCode.INVALID_CHAT_ROOM_TYPE, ex.getErrorCode());
    }

    @Test
    @DisplayName("채팅방에 이미 존재하는 유저")
    void addMemberToTeamChatRoom_throwsIfMemberAlreadyExists() {
        chatRoom.setRoomType(RoomType.TEAM);

        when(chatRoomRepository.findById(chatRoomRequest.getRoomId())).thenReturn(Optional.of(chatRoom));
        when(userRepository.findById(chatRoomRequest.getUserId())).thenReturn(Optional.of(user1));  // 반드시 필요

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class,
                () -> chatRoomService.addMemberToTeamChatRoom(chatRoomRequest));
        assertEquals(ErrorCode.CHATROOM_MEMBER_ALREADY_EXISTS, ex.getErrorCode());
    }

    @Test
    @DisplayName("채팅방 삭제 성공")
    void deleteChatRoom_success() {
        // given
        when(chatRoomRepository.findById(chatRoom.getId())).thenReturn(Optional.of(chatRoom));

        // when
        chatRoomService.deleteChatRoom(chatRoom.getId());

        // then
        verify(chatRoomRepository).findById(chatRoom.getId());
        verify(chatRoomRepository).delete(chatRoom);
    }

    @Test
    @DisplayName("채팅방 삭제 실패 - 채팅방 없음")
    void deleteChatRoom_fail_chatRoomNotFound() {
        // given
        when(chatRoomRepository.findById(999L)).thenReturn(Optional.empty());

        // when
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> chatRoomService.deleteChatRoom(999L)
        );

        // then
        assertEquals(ErrorCode.CHAT_ROOM_NOT_FOUND, exception.getErrorCode());
        verify(chatRoomRepository).findById(999L);
        verify(chatRoomRepository, never()).delete(any());
    }
}

