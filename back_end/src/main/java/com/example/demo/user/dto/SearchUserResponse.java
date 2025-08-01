package com.example.demo.user.dto;

import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.entity.User;
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
public class SearchUserResponse {
    private Long id;
    private String userName;
    private String userProfile;
    private boolean major;
    private Integer lastClass;
    private PositionEnum wantedPosition;
    private Set<TechEnum> techStack;
    private Set<ProjectPrefEnum> projectPref;
    private Set<PersonalPrefEnum> personalPref;
    private String projectExp;
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