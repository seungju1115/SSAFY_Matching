package com.example.demo.chat.dao;

import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.ChatRoomMember;
import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    // 특정 유저가 특정 채팅방에 이미 존재하는지 확인
    boolean existsByUserAndChatRoom(User user, ChatRoom chatRoom);
}
