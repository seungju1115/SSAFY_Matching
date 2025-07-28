package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.chat.service.ChatMessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatMessageService chatMessageService;

    @Test
    @DisplayName("채팅방 메시지 조회 - 성공")
    void getChatMessages_Success() throws Exception {
        Long chatRoomId = 1L;

        ChatMessageResponse msg1 = new ChatMessageResponse(
                101L, chatRoomId, 201L, "Hello", LocalDateTime.now()
        );
        ChatMessageResponse msg2 = new ChatMessageResponse(
                102L, chatRoomId, 202L, "Hi there!", LocalDateTime.now()
        );

        Mockito.when(chatMessageService.getAllMessagesByChatRoom(chatRoomId))
                .thenReturn(List.of(msg1, msg2));

        mockMvc.perform(get("/chatroom/{chatRoomId}/messages", chatRoomId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(101)))
                .andExpect(jsonPath("$[0].chatRoomId", is(chatRoomId.intValue())))
                .andExpect(jsonPath("$[0].senderId", is(201)))
                .andExpect(jsonPath("$[0].message", is("Hello")))
                .andExpect(jsonPath("$[1].id", is(102)))
                .andExpect(jsonPath("$[1].chatRoomId", is(chatRoomId.intValue())))
                .andExpect(jsonPath("$[1].senderId", is(202)))
                .andExpect(jsonPath("$[1].message", is("Hi there!")));
    }
}
