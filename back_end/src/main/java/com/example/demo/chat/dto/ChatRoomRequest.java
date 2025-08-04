package com.example.demo.chat.dto;

import com.example.demo.chat.entity.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "채팅방 생성 요청")
public class ChatRoomRequest {

    @Schema(
            description = "채팅방 타입",
            example = "PRIVATE",
            allowableValues = {"TEAM", "PRIVATE"}
    )
    private RoomType roomType;

    @Schema(description = "채팅방 ID (기존 방 조회시)", example = "1")
    private Long roomId;

    @Schema(description = "첫 번째 사용자 ID (PRIVATE용)", example = "1")
    private Long user1Id;

    @Schema(description = "두 번째 사용자 ID (PRIVATE용)", example = "2")
    private Long user2Id;

    @Schema(description = "팀 ID (TEAM용)", example = "10")
    private Long teamId;

    @Schema(description = "사용자 ID (TEAM용)", example = "1")
    private Long userId;
}