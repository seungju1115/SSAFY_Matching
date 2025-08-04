package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatRoomMemberRepository;
import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.ChatRoomMember;
import com.example.demo.chat.entity.RoomType;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChatMemberServiceIntegrationTest {

    @Autowired
    private ChatMemberService chatMemberService;

    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

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

    private User user;
    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        user = createValidUser("tester","tester@example.com");
        user = userRepository.save(user);

        chatRoom = new ChatRoom();
        chatRoom.setRoomType(RoomType.PRIVATE);
        chatRoom = chatRoomRepository.save(chatRoom);
    }

    @Test
    void createChatRoomMember_shouldCreateMember_whenNotExists() {
        ChatRoomMember member = chatMemberService.createChatRoomMember(user, chatRoom);

        assertNotNull(member);
        assertEquals(user.getId(), member.getUser().getId());
        assertEquals(chatRoom.getId(), member.getChatRoom().getId());

        boolean exists = chatRoomMemberRepository.existsByUserAndChatRoom(user, chatRoom);
        assertTrue(exists);
    }

    @Test
    void createChatRoomMember_shouldThrowException_whenMemberExists() {
        chatMemberService.createChatRoomMember(user, chatRoom);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            chatMemberService.createChatRoomMember(user, chatRoom);
        });

        assertEquals(ErrorCode.CHATROOM_MEMBER_ALREADY_EXISTS, exception.getErrorCode());
    }
}
