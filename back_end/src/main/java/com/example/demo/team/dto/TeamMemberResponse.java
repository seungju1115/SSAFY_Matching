package com.example.demo.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "팀원 정보 응답")
public class TeamMemberResponse {

    @Schema(description = "팀원 사용자 ID", example = "12")
    private Long memberId;

    @Schema(description = "팀원 사용자명", example = "김개발")
    private String username;
}