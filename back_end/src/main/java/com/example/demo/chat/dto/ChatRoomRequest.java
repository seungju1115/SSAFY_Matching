package com.example.demo.chat.dto;

import com.example.demo.chat.entity.RoomType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomRequest {
    private RoomType roomType;   // TEAM or PRIVATE
    private Long roomId;
    // ✅ PRIVATE 일 때는 user1Id, user2Id 필요
    private Long user1Id;
    private Long user2Id;

    // ✅ TEAM 일 때는 teamId, creatorUserId 필요
    private Long teamId;
    private Long userId;
}