package com.example.demo.user.dto;

import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 프로필 수정 요청 (부분 수정)")
public class UserProfileUpdateRequest {

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "자기소개", example = "수정된 자기소개입니다")
    private String userProfile;

    @Schema(description = "전공 여부", example = "true")
    private boolean major;

    @Schema(description = "기수", example = "13")
    private Integer lastClass;

    @Schema(description = "희망 포지션", example = "FULLSTACK")
    private PositionEnum wantedPosition;

    @Schema(description = "프로젝트 선호도", example = "[\"WEB\", \"AI\"]")
    private Set<ProjectPrefEnum> projectPref;

    @Schema(description = "개인 성향", example = "[\"CREATIVE\", \"ANALYTICAL\"]")
    private Set<PersonalPrefEnum> personalPref;

    @Schema(description = "프로젝트 경험", example = "추가된 프로젝트 경험")
    private String projectExp;

    @Schema(description = "자격증", example = "추가 자격증")
    private String qualification;

    @Schema(description = "기술 스택", example = "[\"JAVA\", \"REACT\", \"PYTHON\"]")
    private Set<TechEnum> techStack;

    @Schema(description = "변경할 팀 ID", example = "10")
    private Long teamId;
}