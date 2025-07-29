package com.example.demo.auth.dto;

import com.example.demo.auth.Enum.PersonalPrefEnum;
import com.example.demo.auth.Enum.ProjectPrefEnum;
import com.example.demo.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {
    private String major;
    private Integer lastClass;
    private String wantedPosition;
    private String projectPref;
    private String personalPref;
    private String projectExp;
    private String qualification;
    private String techStack;

    public static User toEntity(UserProfileRequest userProfileRequest) {
        User user = new User();
        user.setMajor(userProfileRequest.getMajor());
        user.setLastClass(userProfileRequest.getLastClass());
        user.setWantedPosition(userProfileRequest.getWantedPosition());
        //user.setPersonalPref(PersonalPrefEnum.valueOf(userProfileRequest.getPersonalPref()));
        //user.setProjectPref(ProjectPrefEnum.valueOf(userProfileRequest.getProjectPref()));
        user.setProjectExp(userProfileRequest.getProjectExp());
        user.setQualification(userProfileRequest.getQualification());
        //user.setTechStack();
        return user;
    }

}
