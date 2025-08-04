package com.example.demo.user.dto;

import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.entity.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {
    @NotBlank(message = "이름은 필수입니다.")
    private String userName;

    private String userProfile;

    private boolean major;

    @NotNull(message = "기수는 필수입니다.")
    @Min(value = 1, message = "기수는 1 이상이어야 합니다.")
    private Integer lastClass;

    @NotNull(message = "희망 포지션은 필수입니다.")
    private PositionEnum wantedPosition;

    @Size(max = 5, message = "최대 5개의 프로젝트 성향만 선택 가능합니다.")
    private Set<ProjectPrefEnum> projectPref;

    @Size(max = 5, message = "최대 5개의 성격 성향만 선택 가능합니다.")
    private Set<PersonalPrefEnum> personalPref;

    private String projectExp;

    private String qualification;

    @Size(max = 10, message = "기술 스택은 최대 10개까지 선택 가능합니다.")
    private Set<TechEnum> techStack;


    public static User toEntity(UserProfileRequest request) {
        User user = new User();
        user.setUserName(request.getUserName());
        user.setUserProfile(request.getUserProfile());
        user.setMajor(request.isMajor());
        user.setLastClass(request.getLastClass());
        user.setWantedPosition(request.getWantedPosition());
        user.setProjectPref(request.getProjectPref());
        user.setPersonalPref(request.getPersonalPref());
        user.setProjectExp(request.getProjectExp());
        user.setQualification(request.getQualification());
        user.setTechStack(request.getTechStack());
        return user;
    }
}
