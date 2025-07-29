package com.example.demo.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String major;
    private Integer lastClass;
    private String wantedPosition;
    private String projectPref;
    private String personalPref;
    private String projectExp;
    private String qualification;
    private String techStack;
}
