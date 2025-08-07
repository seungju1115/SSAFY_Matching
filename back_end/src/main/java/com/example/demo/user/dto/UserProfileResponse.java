package com.example.demo.user.dto;

import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.*;
import com.example.demo.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 프로필 응답")
public class UserProfileResponse implements Serializable {

    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "역할", example = "Admin")
    private String role;

    @Schema(description = "이메일", example = "hong@example.com")
    private String email;

    @Schema(description = "자기소개", example = "안녕하세요! 백엔드 개발자를 꿈꾸는 홍길동입니다.")
    private String userProfile;

    @Schema(description = "전공 여부", example = "true")
    private boolean major;

    @Schema(description = "이전 반", example = "7")
    private Integer lastClass;

    @Schema(description = "희망 포지션", example = "BACKEND")
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

    @Schema(description = "소속 팀 ID", example = "5")
    private Long teamId;

    @Schema(description = "소속 팀 이름", example = "프로젝트팀A")
    private String teamName;

    public static UserProfileResponse toUserProfileResponse(User user) {
        Team team = user.getTeam();

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setUserName(user.getUserName());
        response.setRole(user.getRole());
        response.setEmail(user.getEmail());
        response.setMajor(user.isMajor());
        response.setLastClass(user.getLastClass());

        if (user.getUserProfile() != null) response.setUserProfile(user.getUserProfile());
        if (user.getWantedPosition() != null) response.setWantedPosition(user.getWantedPosition());
        if (user.getProjectGoal() != null) response.setProjectGoal(user.getProjectGoal());
        if (user.getProjectVive() != null) response.setProjectVive(user.getProjectVive());
        if (user.getProjectExp() != null) response.setProjectExp(user.getProjectExp());
        if (user.getQualification() != null) response.setQualification(user.getQualification());
        if (user.getTechStack() != null) response.setTechStack(user.getTechStack());

        if (team != null) {
            response.setTeamId(team.getId());
            if (team.getTeamName() != null) response.setTeamName(team.getTeamName());
        }

        return response;
    }
}