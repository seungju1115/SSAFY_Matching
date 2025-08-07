package com.example.demo.team.dto;

import com.example.demo.team.entity.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "팀 가입/초대 요청")
public class TeamOffer {

    @Schema(
            description = "요청 타입",
            example = "JOIN",
            allowableValues = {"JOIN", "INVITE"},
            required = true
    )
    @NotNull(message = "요청 타입은 필수입니다.")
    public RequestType requestType;

    @Schema(
            description = "대상 사용자 ID",
            example = "12",
            required = true
    )
    @NotNull(message = "사용자 ID는 필수입니다.")
    public Long userId;

    @Schema(
            description = "요청 메시지 (선택사항)",
            example = "함께 프로젝트를 진행하고 싶습니다!",
            maxLength = 200
    )
    @NotBlank(message = "메시지는 필수입니다.")
    public String message;

    @Schema(
            description = "대상 팀 ID",
            example = "1",
            required = true
    )
    @NotNull(message = "팀 ID는 필수입니다.")
    public Long teamId;
}