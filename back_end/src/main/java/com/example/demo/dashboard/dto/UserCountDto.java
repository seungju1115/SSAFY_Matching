package com.example.demo.dashboard.dto;

import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

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
    private Boolean major;

    @Schema(description = "희망 포지션", example = "BACKEND")
    private List<PositionEnum> position;

    public static UserCountDto from(User user) {
        UserCountDto dto = new UserCountDto();
        dto.team = user.getTeam();
        dto.major = user.getMajor();
        dto.position=user.getWantedPosition();
        return dto;
    }
}