package com.example.demo.user.dto;

import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "팀원 검색 요청")
public class SearchUserRequest {

    @Schema(
            description = "희망 포지션",
            example = "BACKEND",
            implementation = PositionEnum.class
    )
    private PositionEnum wantedPosition;

    @Schema(
            description = "기술 스택 (교집합 매칭)",
            example = "[\"JAVA\", \"SPRING\", \"MYSQL\"]"
    )
    private Set<TechEnum> techStack;

    @Schema(
            description = "프로젝트 선호도 (교집합 매칭)",
            example = "[\"WEB\", \"MOBILE\"]"
    )
    private Set<ProjectPrefEnum> projectPref;
}