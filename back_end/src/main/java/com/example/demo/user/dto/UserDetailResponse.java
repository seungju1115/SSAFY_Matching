package com.example.demo.user.dto;

import com.example.demo.user.Enum.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.example.demo.user.entity.User;
import com.example.demo.team.entity.Team;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 상세 응답 DTO")
public class UserDetailResponse {

    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "역할", example = "student")
    private String role;

    @Schema(description = "이메일", example = "hong@example.com")
    private String email;

    @Schema(description = "전공 여부", example = "true")
    private boolean major;

    @Schema(description = "이전 반", example = "7")
    private Integer lastClass;

    @Schema(description = "자기소개", example = "안녕하세요! 백엔드 개발자를 꿈꾸는 홍길동입니다.")
    private String userProfile;

    @Schema(description = "희망 포지션 목록")
    private List<PositionEnum> wantedPosition;

    @Schema(description = "프로젝트 선호도")
    private Set<ProjectGoalEnum> projectGoal;

    @Schema(description = "개인 성향")
    private Set<ProjectViveEnum> projectVive;

    @Schema(description = "프로젝트 경험", example = "Spring Boot를 이용한 웹 개발 경험")
    private String projectExp;

    @Schema(description = "자격증", example = "정보처리기사")
    private String qualification;

    @Schema(description = "기술 스택")
    private Set<TechEnum> techStack;

    public static UserDetailResponse fromEntity(User user) {
        if (user == null) return null;

        return UserDetailResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .role(user.getRole())
                .email(user.getEmail())
                .major(user.isMajor())
                .lastClass(user.getLastClass())
                .userProfile(user.getUserProfile())
                .wantedPosition(user.getWantedPosition())
                .projectGoal(user.getProjectGoal())
                .projectVive(user.getProjectVive())
                .projectExp(user.getProjectExp())
                .qualification(user.getQualification())
                .techStack(user.getTechStack())
                .build();
    }

}