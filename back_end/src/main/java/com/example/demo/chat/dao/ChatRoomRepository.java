package com.example.demo.chat.dao;

import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}