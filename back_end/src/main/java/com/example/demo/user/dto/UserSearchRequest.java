package com.example.demo.user.dto;

import com.example.demo.user.Enum.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "팀원 검색 요청")
public class UserSearchRequest {

    @Schema(
            description = "전공 비전공",
            example = "true"
    )
    private Boolean major;

    @Schema(
            description = "희망 포지션",
            example = "[\"BACKEND\", \"AI\"]",
            implementation = PositionEnum.class
    )
    private List<PositionEnum> wantedPosition;

    @Schema(
            description = "기술 스택",
            example = "[\"JAVA\", \"SPRING\", \"MYSQL\"]"
    )
    private Set<TechEnum> techStack;

    @Schema(
            description = "개인 성향",
            example = "[\"CASUAL\", \"FORMAL\", \"BRANDNEW\"]"
    )
    private Set<ProjectViveEnum> projectVive;

    @Schema(
            description = "프로젝트 성향",
            example = "[\"JOB\", \"AWARD\"]"
    )
    private Set<ProjectGoalEnum> projectGoal;

    @Schema(
            description = "유저 상태",
            example = "[\"INACTIVE\", \"WAITING\", \"IN_TEAM\"]"
    )
    private UserStatus userStatus;
}