package com.example.demo.chat.dao;

import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.ChatRoomMember;
import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    boolean existsByUserAndChatRoom(User user, ChatRoom chatRoom);

    Optional<ChatRoomMember> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);
}
