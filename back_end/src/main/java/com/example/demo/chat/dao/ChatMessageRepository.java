package com.example.demo.chat.dao;

import com.example.demo.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // ✅ 특정 채팅방의 메시지를 생성일 순으로 조회 (추가 예시)
    List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);
}
