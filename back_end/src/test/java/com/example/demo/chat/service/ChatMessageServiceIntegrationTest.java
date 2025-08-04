package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatMessageRepository;
import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.chat.dto.ChatMessageRequest;
import com.example.demo.chat.entity.ChatMessage;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.RoomType;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  // 테스트 끝나면 롤백
class ChatMessageServiceIntegrationTest {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private ChatRoom chatRoom;
    private User sender;

    @BeforeEach
    void setUp() {
        // 테스트용 데이터 미리 저장
        chatRoom = new ChatRoom();
        chatRoom.setRoomType(RoomType.PRIVATE);
        chatRoom = chatRoomRepository.save(chatRoom);

        sender = new User();
        sender.setUserName("테스트유저");
        sender.setEmail("test@example.com");
        sender.setRole("USER");
        // 필수 필드들 세팅 (유효성 문제 피하기 위해)
        sender.setProjectPref(Set.of(ProjectPrefEnum.CHALLENGE));
        sender.setPersonalPref(Set.of(PersonalPrefEnum.CONCENTRATE));
        sender.setWantedPosition(PositionEnum.BACKEND);  // enum 등으로 대체
        sender.setTechStack(Set.of(TechEnum.MyBatis));
        sender.setLastClass(13);
        userRepository.save(sender);
    }

    @Test
    void saveMessage_success() {
        ChatMessageRequest request = new ChatMessageRequest();
        request.setRoomId(chatRoom.getId());
        request.setSenderId(sender.getId());
        request.setMessage("Hello Integration Test");

        var response = chatMessageService.saveMessage(request);

        assertNotNull(response);
        assertEquals("Hello Integration Test", response.getMessage());

        // DB 저장 확인
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoom.getId());
        assertTrue(messages.stream().anyMatch(m -> m.getMessage().equals("Hello Integration Test")));
    }

    @Test
    void getAllMessagesByChatRoom_success() {
        ChatMessage msg1 = new ChatMessage();
        msg1.setChatRoom(chatRoom);
        msg1.setSender(sender);
        msg1.setMessage("First Message");
        msg1.setCreatedAt(LocalDateTime.now());
        chatMessageRepository.save(msg1);

        ChatMessage msg2 = new ChatMessage();
        msg2.setChatRoom(chatRoom);
        msg2.setSender(sender);
        msg2.setMessage("Second Message");
        msg2.setCreatedAt(LocalDateTime.now());
        chatMessageRepository.save(msg2);

        var responses = chatMessageService.getAllMessagesByChatRoom(chatRoom.getId());
        assertEquals(2, responses.size());
        assertEquals("First Message", responses.get(0).getMessage());
        assertEquals("Second Message", responses.get(1).getMessage());
    }
}
