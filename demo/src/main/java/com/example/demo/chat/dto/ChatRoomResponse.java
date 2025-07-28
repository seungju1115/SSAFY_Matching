package com.example.demo.chat.dto;

import com.example.demo.chat.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRoomResponse {
    private Long roomId;            // 채팅방 ID
    private RoomType roomType;      // TEAM or PRIVATE
    private Long teamId;            // PRIVATE 이면 null

//    private List<String> memberNames;  // 참여자 이름 목록
}