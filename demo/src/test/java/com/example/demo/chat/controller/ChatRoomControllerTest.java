package com.example.demo.chat.controller;

import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.dto.ChatRoomResponse;
import com.example.demo.chat.entity.RoomType;
import com.example.demo.chat.service.ChatMessageService;
import com.example.demo.chat.service.ChatRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatMessageService chatMessageService;

    @MockBean
    private ChatRoomService chatRoomService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("채팅방 메시지 전체 조회")
    void getChatMessages() throws Exception {
        // given
        ChatMessageResponse message1 = new ChatMessageResponse(1L, 1L, 1L, "안녕하세요", LocalDateTime.of(2025, 7, 28, 10, 0, 0));
        ChatMessageResponse message2 = new ChatMessageResponse(2L, 1L, 2L, "반갑습니다", LocalDateTime.of(2025, 7, 28, 10, 1, 0));

        Mockito.when(chatMessageService.getAllMessagesByChatRoom(1L))
                .thenReturn(List.of(message1, message2));

        // when & then
        mockMvc.perform(get("/chatroom/1/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()", is(2)))
                .andExpect(jsonPath("$.data.[0].message", is("안녕하세요")))
                .andExpect(jsonPath("$.data.[1].senderId", is(2)));
    }

    @Test
    @DisplayName("1:1 채팅방 생성")
    void createPrivateChatRoom() throws Exception {
        // given
        ChatRoomRequest request = new ChatRoomRequest();
        request.setUser1Id(1L);
        request.setUser2Id(2L);

        ChatRoomResponse response = new ChatRoomResponse(100L, RoomType.PRIVATE, null);

        Mockito.when(chatRoomService.createPrivateChatRoom(any(ChatRoomRequest.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(post("/chatroom/private")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.roomId", is(100)))
                .andExpect(jsonPath("$.data.roomType", is("PRIVATE")));
    }
}
