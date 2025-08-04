package com.example.demo.user.dto;

import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
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
public class SearchUserResponse {

    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 이름", example = "김개발")
    private String userName;

    @Schema(description = "자기소개", example = "프론트엔드 개발자입니다")
    private String userProfile;

    @Schema(description = "전공 여부", example = "true")
    private boolean major;

    @Schema(description = "기수", example = "13")
    private Integer lastClass;

    @Schema(description = "희망 포지션", example = "FRONTEND")
    private PositionEnum wantedPosition;

    @Schema(description = "보유 기술 스택")
    private Set<TechEnum> techStack;

    @Schema(description = "프로젝트 선호도")
    private Set<ProjectPrefEnum> projectPref;

    @Schema(description = "개인 성향")
    private Set<PersonalPrefEnum> personalPref;

    @Schema(description = "프로젝트 경험", example = "React 기반 SPA 개발 경험")
    private String projectExp;

    @Schema(description = "자격증", example = "웹디자인기능사")
    private String qualification;

    public static SearchUserResponse fromUser(User user) {
        SearchUserResponse dto = new SearchUserResponse();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setUserProfile(user.getUserProfile());
        dto.setMajor(user.isMajor());
        dto.setLastClass(user.getLastClass());
        dto.setWantedPosition(user.getWantedPosition());
        dto.setTechStack(user.getTechStack());
        dto.setProjectPref(user.getProjectPref());
        dto.setPersonalPref(user.getPersonalPref());
        dto.setProjectExp(user.getProjectExp());
        dto.setQualification(user.getQualification());
        return dto;
    }

    public static List<SearchUserResponse> fromUserList(List<User> users) {
        return users.stream()
                .map(SearchUserResponse::fromUser)
                .collect(Collectors.toList());
    }
}
