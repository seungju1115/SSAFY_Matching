package com.example.demo.user.dto;

import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String userName;
    private String role;
    private String email;
    private String userProfile;
    private String major;
    private Integer lastClass;
    private String wantedPosition;
    private Set<ProjectPrefEnum> projectPref;
    private Set<PersonalPrefEnum> personalPref;
    private String projectExp;
    private String qualification;
    private Set<TechEnum> techStack;

    private Long teamId;
    private String teamName;

    public static UserProfileResponse toUserProfileResponse(User user) {
        Team team = user.getTeam();

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setUserName(user.getUserName());
        response.setRole(user.getRole());
        response.setEmail(user.getEmail());
        response.setUserProfile(user.getUserProfile());
        response.setMajor(user.getMajor());
        response.setLastClass(user.getLastClass());
        response.setWantedPosition(user.getWantedPosition());
        response.setProjectPref(user.getProjectPref());
        response.setPersonalPref(user.getPersonalPref());
        response.setProjectExp(user.getProjectExp());
        response.setQualification(user.getQualification());
        response.setTechStack(user.getTechStack());

        if (team != null) {
            response.setTeamId(team.getId());
            response.setTeamName(team.getTeamName());
        }

        return response;
    }
}
