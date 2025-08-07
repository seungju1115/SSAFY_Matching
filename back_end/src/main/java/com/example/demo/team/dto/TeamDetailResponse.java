package com.example.demo.team.dto;

import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.dto.SearchUserResponse;
import com.example.demo.user.dto.UserProfileResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "팀 상세 정보 응답")
public class TeamDetailResponse implements Serializable {

    @Schema(description = "팀 ID", example = "1")
    private Long teamId;

    @Schema(description = "채팅방 Id", example = "1")
    private Long chatRoomId;

    @Schema(description = "팀 이름", example = "프로젝트 A팀")
    private String teamName;

    @Schema(description = "팀장 정보", example = "leader")
    private UserProfileResponse leader;

    @Schema(
            description = "팀원 정보 목록",
            example = "[member1, member2]"
    )
    private List<UserProfileResponse> members;

    @Schema(
            description = "팀 도메인",
            example = "domainTest11",
            required = true
    )
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
    private int backendCount;

    @Schema(description = "프론트엔드 개발자 인원", example = "2")
    private int frontendCount;

    @Schema(description = "AI 개발자 인원", example = "1")
    private int aiCount;

    @Schema(description = "기획자(PM) 인원", example = "1")
    private int pmCount;

    @Schema(description = "디자이너 인원", example = "0")
    private int designCount;

    @Schema(
            description = "팀 상세 설명",
            example = "저희는 사이드 프로젝트를 통해 함께 성장하고 싶은 주니어 개발자 그룹입니다."
    )
    private String teamDescription;
}