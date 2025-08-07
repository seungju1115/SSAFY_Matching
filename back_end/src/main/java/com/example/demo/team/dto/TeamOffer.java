package com.example.demo.team.dto;

import com.example.demo.team.entity.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
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
    public RequestType requestType;

    @Schema(
            description = "대상 사용자 ID",
            example = "12",
            required = true
    )
    public Long userId;

    @Schema(
            description = "요청 메시지 (선택사항)",
            example = "함께 프로젝트를 진행하고 싶습니다!",
            maxLength = 200
    )
    public String message;

    @Schema(
            description = "대상 팀 ID",
            example = "1",
            required = true
    )
    public Long teamId;
}