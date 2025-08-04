package com.example.demo.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "팀 생성/수정/검색 요청")
public class TeamRequest {

    @Schema(description = "팀 ID (수정/검색 시 사용)", example = "1")
    private Long teamId;

    @Schema(
            description = "팀 이름",
            example = "프로젝트 A팀",
            required = true,
            minLength = 2,
            maxLength = 20
    )
    private String teamName;

    @Schema(
            description = "팀장 사용자 ID",
            example = "5",
            required = true
    )
    private Long leaderId;
}
