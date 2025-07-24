package com.example.demo.chat.service;

import com.example.demo.chat.dto.ChatMessage;
import com.example.demo.chat.dto.ChatRoom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

class ChatServiceTest {

    private ChatService chatService;
    private final Map<String, ChatRoom> chatRooms = new LinkedHashMap<>();
    private static ChatMessage chatMessage;

    @BeforeAll
    static void setUp(){
        chatMessage = new ChatMessage();
        chatMessage.setType(false);
        chatMessage.setMessage("hi");
        chatMessage.setDateTime("2017 - 01 - 23");
        chatMessage.setSender("승주");
    }

    @Test
    void findAllRoomsTest() {

    }

    @Test
    void createRoomTest() {
    }

    @Test
    void findRoomByIdTest() {
    }

    @Test
    void putMessageTest(){

    }
}