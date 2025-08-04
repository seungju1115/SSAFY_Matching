package com.example.demo.dashboard.dto;

import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.PositionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 카운트 집계용 DTO (내부 사용)")
public class UserCountDto {

    @Schema(description = "소속 팀 정보 (null이면 팀 없음)")
    private Team team;

    @Schema(description = "전공 여부", example = "true")
    private boolean major;

    @Schema(description = "희망 포지션", example = "BACKEND")
    private PositionEnum position;
}