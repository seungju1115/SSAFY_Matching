package com.example.demo.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "팀 수정/검색 요청")
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
    @NotBlank(message = "팀 이름은 필수입니다.")
    private String teamName;

    @Schema(
            description = "팀장 사용자 ID",
            example = "5",
            required = true
    )
    @NotNull(message = "팀장 ID는 필수입니다.")
    private Long leaderId;

    @Schema(
            description = "팀 도메인",
            example = "domainTest11",
            required = true
    )
    @NotNull(message = "도메인은 필수입니다.")
    @Size(min=10)
    private String teamDomain;

}
