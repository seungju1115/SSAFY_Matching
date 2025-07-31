package com.example.demo.chat;

import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.dto.ChatRoomResponse;
import com.example.demo.chat.entity.RoomType;
import com.example.demo.user.entity.User;
import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.chat.service.ChatRoomService;
import com.example.demo.chat.service.ChatMessageService;
import com.example.demo.chat.dto.ChatMessageRequest;
import com.example.demo.chat.dto.ChatMessageResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class DemoChatTest {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createChatRoomAndSendMessage() {
        // 1. 테스트용 유저 생성
        User user1 = new User();
        userRepository.save(user1);

        User user2 = new User();
        userRepository.save(user2);

        // 2. 1:1 채팅방 생성 요청 DTO 만들기
        ChatRoomRequest chatRoomRequest = new ChatRoomRequest();
        chatRoomRequest.setUser1Id(user1.getId());
        chatRoomRequest.setUser2Id(user2.getId());

        // 3. 채팅방 생성 서비스 호출
        ChatRoomResponse chatRoomResponse = chatRoomService.createPrivateChatRoom(chatRoomRequest);
        assertThat(chatRoomResponse).isNotNull();
        assertThat(chatRoomResponse.getRoomType()).isEqualTo(RoomType.PRIVATE);

        Long chatRoomId = chatRoomResponse.getRoomId();

        // 4. 메시지 보내기 위한 요청 DTO 생성
        ChatMessageRequest messageRequest = new ChatMessageRequest(chatRoomId, user1.getId(), "안녕하세요!");

        // 5. 메시지 저장 서비스 호출
        ChatMessageResponse messageResponse = chatMessageService.saveMessage(messageRequest);
        assertThat(messageResponse).isNotNull();
        assertThat(messageResponse.getMessage()).isEqualTo("안녕하세요!");
        assertThat(messageResponse.getChatRoomId()).isEqualTo(chatRoomId);
    }
}
