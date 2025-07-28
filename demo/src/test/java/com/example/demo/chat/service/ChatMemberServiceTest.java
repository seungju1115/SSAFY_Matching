package com.example.demo.chat.service;

import com.example.demo.chat.dao.ChatRoomMemberRepository;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.ChatRoomMember;
import com.example.demo.chat.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ChatMemberServiceTest {

    @Mock
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @InjectMocks
    private ChatMemberService chatMemberService;

    @Test
    void createChatRoomMember_shouldCreateMemberWithUserAndChatRoom() {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(10L);

        // when
        ChatRoomMember member = chatMemberService.createChatRoomMember(user, chatRoom);

        // then
        assertThat(member).isNotNull();
        assertThat(member.getUser()).isEqualTo(user);
        assertThat(member.getChatRoom()).isEqualTo(chatRoom);
    }
}
