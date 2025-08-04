package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.dto.ChatRoomResponse;
import com.example.demo.chat.entity.RoomType;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import com.example.demo.team.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ChatRoomServiceIntegrationTest {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private User user1;
    private User user2;
    private Team team;

    private User createValidUser(String name, String email) {
        User user = new User();
        user.setUserName(name);
        user.setRole("ROLE_USER");
        user.setWantedPosition(PositionEnum.BACKEND);
        user.setTechStack(Set.of(TechEnum.Docker));
        user.setPersonalPref(Set.of(PersonalPrefEnum.CONCENTRATE));
        user.setProjectPref(Set.of(ProjectPrefEnum.CHALLENGE));
        user.setLastClass(13);
        user.setEmail(email);
        return user;
    }

    @BeforeEach
    void setUp() {
        // 사용자 저장
        user1 = createValidUser("user1","user1@gmail.com");
        userRepository.save(user1);

        user2 = new User();
        user2 = createValidUser("user1","user2@gmail.com");
        userRepository.save(user2);

        // 팀 저장
        team = new Team();
        team.setTeamName("teamA");
        team.setTeamDomain("DOMAINwebDesign");
        teamRepository.save(team);
    }

    @Test
    void createPrivateChatRoom_createsAndReturnsRoom() {
        ChatRoomRequest request = new ChatRoomRequest();
        request.setUser1Id(user1.getId());
        request.setUser2Id(user2.getId());
        request.setRoomType(RoomType.PRIVATE);

        ChatRoomResponse response = chatRoomService.createPrivateChatRoom(request);

        assertNotNull(response);
        assertEquals(RoomType.PRIVATE, response.getRoomType());
        assertTrue(response.getRoomId() > 0);

        Optional<ChatRoom> roomOpt = chatRoomRepository.findById(response.getRoomId());
        assertTrue(roomOpt.isPresent());
        ChatRoom room = roomOpt.get();

        assertEquals(RoomType.PRIVATE, room.getRoomType());
        assertEquals(2, room.getMembers().size());
    }

    @Test
    void createTeamChatRoom_createsAndReturnsRoom() {
        ChatRoomRequest request = new ChatRoomRequest();
        request.setTeamId(team.getId());
        request.setUserId(user1.getId());
        request.setRoomType(RoomType.TEAM);

        ChatRoomResponse response = chatRoomService.createTeamChatRoom(request);

        assertNotNull(response);
        assertEquals(RoomType.TEAM, response.getRoomType());
        assertEquals(team.getId(), response.getTeamId());

        Optional<ChatRoom> roomOpt = chatRoomRepository.findById(response.getRoomId());
        assertTrue(roomOpt.isPresent());
        ChatRoom room = roomOpt.get();

        assertEquals(RoomType.TEAM, room.getRoomType());
        assertEquals(team.getId(), room.getTeam().getId());
        assertEquals(1, room.getMembers().size());
    }

    @Test
    void addMemberToTeamChatRoom_addsNewMember() {
        // 먼저 팀 채팅방 생성
        ChatRoomRequest createRequest = new ChatRoomRequest();
        createRequest.setTeamId(team.getId());
        createRequest.setUserId(user1.getId());
        createRequest.setRoomType(RoomType.TEAM);
        ChatRoomResponse createdRoom = chatRoomService.createTeamChatRoom(createRequest);

        // 새 사용자 생성
        User newUser = createValidUser("newUser","newUser@gmail.com");
        userRepository.save(newUser);

        // 멤버 추가 요청
        ChatRoomRequest addRequest = new ChatRoomRequest();
        addRequest.setRoomId(createdRoom.getRoomId());
        addRequest.setUserId(newUser.getId());

        chatRoomService.addMemberToTeamChatRoom(addRequest);

        ChatRoom chatRoom = chatRoomRepository.findById(createdRoom.getRoomId()).orElseThrow();
        assertEquals(2, chatRoom.getMembers().size());
    }

    @Test
    void deleteChatRoom_deletesRoom() {
        // 채팅방 생성
        ChatRoomRequest request = new ChatRoomRequest();
        request.setUser1Id(user1.getId());
        request.setUser2Id(user2.getId());
        request.setRoomType(RoomType.PRIVATE);

        ChatRoomResponse response = chatRoomService.createPrivateChatRoom(request);

        Long roomId = response.getRoomId();

        assertTrue(chatRoomRepository.findById(roomId).isPresent());

        chatRoomService.deleteChatRoom(roomId);

        assertFalse(chatRoomRepository.findById(roomId).isPresent());
    }
}
