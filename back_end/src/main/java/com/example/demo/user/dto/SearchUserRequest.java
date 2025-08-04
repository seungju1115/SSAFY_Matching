package com.example.demo.user.dto;

import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
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
public class SearchUserRequest {
    private PositionEnum wantedPosition;

    @Size(max = 10, message = "기술 스택은 최대 10개까지 선택 가능합니다.")
    private Set<TechEnum> techStack;

    @Size(max = 5, message = "선호하는 프로젝트 유형은 최대 5개까지 선택 가능합니다.")
    private Set<ProjectPrefEnum> projectPref;
}