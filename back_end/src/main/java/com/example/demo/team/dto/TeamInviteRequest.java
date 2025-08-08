package com.example.demo.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "팀원 직접 초대 요청")
public class TeamInviteRequest {

    @Schema(
            description = "초대할 사용자 ID",
            example = "12",
            required = true
    )
    @NotNull(message = "초대할 사용자 ID는 필수입니다.")
    private Long userId;

    @Schema(
            description = "초대할 팀 ID",
            example = "1",
            required = true
    )
    @NotNull(message = "팀 ID는 필수입니다.")
    private Long teamId;
}