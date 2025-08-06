package com.example.demo.team.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamInviteRequest {
    @NotNull(message = "초대할 사용자 ID는 필수입니다.")
    private Long userId;

    @NotNull(message = "팀 ID는 필수입니다.")
    private Long teamId;
}
