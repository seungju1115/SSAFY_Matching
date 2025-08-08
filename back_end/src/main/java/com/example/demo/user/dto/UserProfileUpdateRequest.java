package com.example.demo.user.dto;

import com.example.demo.user.Enum.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 프로필 수정 요청 (부분 수정)")
public class UserProfileUpdateRequest implements Serializable {

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "자기소개", example = "수정된 자기소개입니다")
    private String userProfile;

    @Schema(description = "전공 여부", example = "true")
    private boolean major;

    @Schema(description = "이전 반", example = "7")
    private Integer lastClass;

    @Schema(description = "희망 포지션", example = "FULLSTACK")
    private List<PositionEnum> wantedPosition;

    @Schema(description = "프로젝트 선호도", example = "[\"도전적인 성향\", \"새로운 기술 적극 사용\"]")
    private Set<ProjectGoalEnum> projectGoal;

    @Schema(description = "개인 성향", example = "[\"내향적인 편\", \"컨벤션 잘 지키는 편\"]")
    private Set<ProjectViveEnum> projectVive;

    @Schema(description = "프로젝트 경험", example = "추가된 프로젝트 경험")
    private String projectExp;

    @Schema(description = "자격증", example = "추가 자격증")
    private String qualification;

    @Schema(description = "기술 스택", example = "[\"JAVA\", \"REACT\", \"PYTHON\"]")
    private Set<TechEnum> techStack;

    @Schema(description = "변경할 팀 ID", example = "10")
    private Long teamId;
}