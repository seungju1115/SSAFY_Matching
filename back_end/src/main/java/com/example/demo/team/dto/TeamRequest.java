package com.example.demo.team.dto;

import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

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

    @Schema(
            description = "팀 분위기/성향 (Enum Set)",
            example = "[\"ENTHUSIASTIC\", \"CHALLENGING\"]"
    )
    private Set<ProjectViveEnum> teamVive;

    @Schema(
            description = "팀 목표/선호사항 (Enum Set)",
            example = "[\"PORTFOLIO\", \"COMMERCIALIZATION\"]",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Set<ProjectGoalEnum> teamPreference;

    @Schema(description = "백엔드 개발자 인원", example = "2")
    @Min(value = 0, message = "인원수는 0 이상이어야 합니다.")
    private int backendCount;

    @Schema(description = "프론트엔드 개발자 인원", example = "2")
    @Min(value = 0, message = "인원수는 0 이상이어야 합니다.")
    private int frontendCount;

    @Schema(description = "AI 개발자 인원", example = "1")
    @Min(value = 0, message = "인원수는 0 이상이어야 합니다.")
    private int aiCount;

    @Schema(description = "기획자(PM) 인원", example = "1")
    @Min(value = 0, message = "인원수는 0 이상이어야 합니다.")
    private int pmCount;

    @Schema(description = "디자이너 인원", example = "0")
    @Min(value = 0, message = "인원수는 0 이상이어야 합니다.")
    private int designCount;

    @Schema(
            description = "팀 상세 설명",
            example = "저희는 사이드 프로젝트를 통해 함께 성장하고 싶은 주니어 개발자 그룹입니다."
    )
    @Size(max = 300, message = "팀 설명은 최대 300자까지 입력 가능합니다.")
    private String teamDescription;
}
