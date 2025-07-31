package com.example.demo.chat.entity;

import static org.assertj.core.api.Assertions.*;

import com.example.demo.team.entity.Team;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ChatRoomTest {

    @Test
    void testRoomType_SetAndGet() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomType(RoomType.PRIVATE);

        assertThat(chatRoom.getRoomType()).isEqualTo(RoomType.PRIVATE);
    }

    @Test
    void testAddAndRemoveMember() {
        ChatRoom chatRoom = new ChatRoom();
        ChatRoomMember member1 = new ChatRoomMember();
        ChatRoomMember member2 = new ChatRoomMember();

        chatRoom.addMember(member1);
        chatRoom.addMember(member2);

        List<ChatRoomMember> members = chatRoom.getMembers();
        assertThat(members).containsExactly(member1, member2);

        // 연관관계 설정 확인
        assertThat(member1.getChatRoom()).isEqualTo(chatRoom);
        assertThat(member2.getChatRoom()).isEqualTo(chatRoom);

        // 멤버 제거 테스트
        chatRoom.removeMember(member1);
        assertThat(chatRoom.getMembers()).containsExactly(member2);
        assertThat(member1.getChatRoom()).isNull();
    }

    @Test
    void testSetTeamBidirectional() {
        ChatRoom chatRoom = new ChatRoom();
        Team team = new Team();

        chatRoom.setTeam(team);

        assertThat(chatRoom.getTeam()).isEqualTo(team);
        assertThat(team.getChatRoom()).isEqualTo(chatRoom);
    }
}
