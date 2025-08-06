package com.example.demo.user.dto;

import com.example.demo.user.Enum.*;
import com.example.demo.user.entity.User;
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
@Schema(description = "사용자 프로필 생성 요청")
public class UserProfileRequest {

    @Schema(description = "사용자 이름", example = "홍길동", required = true)
    private String userName;

    @Schema(description = "자기소개", example = "안녕하세요! 백엔드 개발자를 꿈꾸는 홍길동입니다.")
    private String userProfile;

    @Schema(description = "전공 여부", example = "true", required = true)
    private boolean major;

    @Schema(description = "이전 반", example = "7", required = true)
    private Integer lastClass;

    @Schema(description = "희망 포지션", example = "BACKEND", required = true)
    private PositionEnum wantedPosition;

    @Schema(description = "프로젝트 선호도", example = "[\"도전적인 성향\", \"새로운 기술 적극 사용\"]")
    private Set<ProjectGoalEnum> projectGoal;

    @Schema(description = "개인 성향", example = "[\"내향적인 편\", \"컨벤션 잘 지키는 편\"]")
    private Set<ProjectViveEnum> projectVive;

    @Schema(description = "프로젝트 경험", example = "Spring Boot를 이용한 웹 개발 경험")
    private String projectExp;

    @Schema(description = "자격증", example = "정보처리기사")
    private String qualification;

    @Schema(description = "기술 스택", example = "[\"JAVA\", \"SPRING\", \"MYSQL\"]")
    private Set<TechEnum> techStack;

    public static User toEntity(UserProfileRequest request) {
        User user = new User();
        user.setUserName(request.getUserName());
        user.setUserProfile(request.getUserProfile());
        user.setMajor(request.isMajor());
        user.setLastClass(request.getLastClass());
        user.setWantedPosition(request.getWantedPosition());
        user.setProjectGoal(request.getProjectGoal());
        user.setProjectVive(request.getProjectVive());
        user.setProjectExp(request.getProjectExp());
        user.setQualification(request.getQualification());
        user.setTechStack(request.getTechStack());
        return user;
    }
}
