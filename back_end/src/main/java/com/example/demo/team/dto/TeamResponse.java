package com.example.demo.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "팀 기본 정보 응답")
public class TeamResponse {

    @Schema(description = "팀 ID", example = "1")
    private Long teamId;

    @Schema(description = "팀 이름", example = "프로젝트 A팀")
    private String teamName;

    @Schema(description = "팀장 사용자 ID", example = "5")
    private Long leaderId;

    @Schema(description = "현재 팀원 수", example = "3")
    private int memberCount;
}
