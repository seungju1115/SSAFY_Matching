package com.example.demo.chat.dto;

import com.example.demo.chat.entity.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Schema(description = "채팅방 응답")
public class ChatRoomResponse {

    @Schema(description = "채팅방 ID", example = "1")
    private Long roomId;

    @Schema(description = "채팅방 타입", example = "PRIVATE")
    private RoomType roomType;

    @Schema(description = "팀 ID (TEAM 타입일 때만)", example = "10")
    private Long teamId;
//    private List<String> memberNames;  // 참여자 이름 목록
}