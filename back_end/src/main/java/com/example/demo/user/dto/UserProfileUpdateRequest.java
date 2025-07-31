package com.example.demo.user.dto;

import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateRequest {
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

    private Long teamId;
}
