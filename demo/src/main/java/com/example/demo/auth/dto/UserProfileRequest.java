package com.example.demo.auth.dto;

import com.example.demo.auth.Enum.PersonalPrefEnum;
import com.example.demo.auth.Enum.ProjectPrefEnum;
import com.example.demo.auth.Enum.TechEnum;
import com.example.demo.auth.entity.User;
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
    private String userName;
    private String userProfile;
    private String major;
    private Integer lastClass;
    private String wantedPosition;
    private Set<ProjectPrefEnum> projectPref;
    private Set<PersonalPrefEnum> personalPref;
    private String projectExp;
    private String qualification;
    private Set<TechEnum> techStack;

    public static User toEntity(UserProfileRequest request) {
        User user = new User();
        user.setUserName(request.getUserName());
        user.setUserProfile(request.getUserProfile());
        user.setMajor(request.getMajor());
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
