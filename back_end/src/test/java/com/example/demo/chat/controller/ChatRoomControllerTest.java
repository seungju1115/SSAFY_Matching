package com.example.demo.chat.controller;

import com.example.demo.auth.filter.JwtFilter;
import com.example.demo.auth.util.JwtUtil;
import com.example.demo.chat.dto.ChatMessageResponse;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.entity.RoomType;
import com.example.demo.chat.service.ChatMessageService;
import com.example.demo.chat.service.ChatRoomService;
import com.example.demo.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChatRoomService chatRoomService;

    @MockitoBean
    private ChatMessageService chatMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtFilter jwtFilter; // JwtFilter 자체를 mock

    @MockitoBean
    private JwtUtil jwtUtil; // JwtUtil도 mock해버리면 해결됨

    private ChatRoomRequest chatRoomRequest;
    private List<ChatMessageResponse> mockMessages;
    @BeforeEach
    void setUp(){
        chatRoomRequest = new ChatRoomRequest();
        chatRoomRequest.setUser1Id(1L);
        chatRoomRequest.setUser2Id(2L);
        ChatMessageResponse message1 =
                new ChatMessageResponse(1L, 1L, 1L, "Hellow",
                        LocalDateTime.parse("2025-08-06T10:00:00"));

        ChatMessageResponse message2 =
                new ChatMessageResponse(2L, 2L, 2L, "Bye",
                        LocalDateTime.parse("2025-08-06T15:30:00"));
        mockMessages = List.of(message1, message2);
    }

    @Test
    @DisplayName("채팅방 메시지 조회 성공")
    void getChatMessages_success() throws Exception {
        // given
        Long chatRoomId = 1L;
        when(chatMessageService.getAllMessagesByChatRoom(chatRoomId))
                .thenReturn(mockMessages);

        mockMvc.perform(get("/chatroom/{chatRoomId}/messages",1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 실제 서비스 동작까지 목킹하지 않았음
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()))
                .andExpect(jsonPath("$.data[0].chatRoomId").value(1L));
    }

    @Test
    @DisplayName("roomType 포함 시 정상 응답")
    void createPrivateChatRoom_shouldSucceed_whenRoomTypePresent() throws Exception {
        chatRoomRequest.setRoomType(RoomType.PRIVATE);

        mockMvc.perform(post("/chatroom/private")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatRoomRequest)))
                .andExpect(status().isOk()); // 실제 서비스 동작까지 목킹하지 않았음
    }

    @Test
    @DisplayName("roomType 누락 시 400 에러 발생")
    void createPrivateChatRoom_shouldReturn400_whenRoomTypeMissing() throws Exception {
        mockMvc.perform(post("/chatroom/private")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatRoomRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists()); // 커스텀 응답일 경우 메시지 확인
    }

}
