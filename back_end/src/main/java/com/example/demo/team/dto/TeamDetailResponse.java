package com.example.demo.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "팀 상세 정보 응답")
public class TeamDetailResponse implements Serializable {

    @Schema(description = "팀 ID", example = "1")
    private Long teamId;

    @Schema(description =  "채팅방 Id",example = "1")
    private Long chatRoomId;

    @Schema(description = "팀 이름", example = "프로젝트 A팀")
    private String teamName;

    @Schema(description = "팀장 사용자 ID", example = "5")
    private Long leaderId;

    @Schema(
            description = "팀원 ID 목록",
            example = "[5, 12, 18, 23]"
    )
    private List<Long> membersId;



}