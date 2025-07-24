package com.example.demo.chat.service;


import com.example.demo.chat.dto.ChatMessage;
import com.example.demo.chat.dto.ChatRoom;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {
    private final Map<String, ChatRoom> chatRooms = new LinkedHashMap<>();

    public List<ChatRoom> findAllRooms() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom createRoom(String name) {
        String roomId = UUID.randomUUID().toString();
        ChatRoom room = new ChatRoom(roomId, name);
        chatRooms.put(roomId, room);
        return room;
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public void putMessage(ChatMessage chatMessage) {

    }
}