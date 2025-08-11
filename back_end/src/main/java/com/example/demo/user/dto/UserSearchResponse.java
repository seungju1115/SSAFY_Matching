package com.example.demo.user.dto;

import com.example.demo.user.Enum.*;
import com.example.demo.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "팀원 검색 결과")
public class UserSearchResponse {

    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이름", example = "김개발")
    private String userName;

    @Schema(description = "자기소개", example = "프론트엔드 개발자입니다")
    private String userProfile;

    @Schema(description = "전공 여부", example = "true")
    private Boolean major;

    @Schema(description = "이전 반", example = "7")
    private Integer lastClass;

    @Schema(description = "희망 포지션", example = "FRONTEND")
    private List<PositionEnum> wantedPosition;

    @Schema(description = "보유 기술 스택")
    private Set<TechEnum> techStack;

    @Schema(description = "프로젝트 선호도")
    private Set<ProjectGoalEnum> projectGoal;

    @Schema(description = "개인 성향")
    private Set<ProjectViveEnum> projectVive;

    @Schema(description = "프로젝트 경험", example = "React 기반 SPA 개발 경험")
    private String projectExp;

    @Schema(description = "자격증", example = "웹디자인기능사")
    private String qualification;

    public static UserSearchResponse fromUser(User user) {
        UserSearchResponse response = new UserSearchResponse();
        response.setId(user.getId());
        response.setUserName(user.getUserName());
        response.setUserProfile(user.getUserProfile());
        response.setMajor(user.getMajor());
        response.setLastClass(user.getLastClass());
        response.setWantedPosition(user.getWantedPosition());
        response.setTechStack(user.getTechStack());
        response.setProjectGoal(user.getProjectGoal());
        response.setProjectVive(user.getProjectVive());
        response.setProjectExp(user.getProjectExp());
        response.setQualification(user.getQualification());
        return response;
    }

    public static List<UserSearchResponse> fromUserList(List<User> users) {
        return users.stream()
                .map(UserSearchResponse::fromUser)
                .collect(Collectors.toList());
    }
}
