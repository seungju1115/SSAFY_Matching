package com.example.demo.ai.dto;

import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.entity.User;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CandidateDto {

    private Long userId;
    private String userName;
    private String mainPos;
    private String subPos;
    private Set<ProjectGoalEnum> goals;
    private Set<ProjectViveEnum> vives;

    public CandidateDto(Long userId, String userName, List<PositionEnum> position, Set<ProjectGoalEnum> goals, Set<ProjectViveEnum> vives) {
        this.userId = userId;
        this.userName = userName;
        this.goals = goals;
        this.vives = vives;
        this.mainPos = position.get(0).name();
        this.subPos = position.get(1).name();
    }

    public static CandidateDto from(User user) {
        CandidateDto dto=new CandidateDto();
        dto.setUserId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setGoals(user.getProjectGoal());
        dto.setVives(user.getProjectVive());
        dto.setMainPos(user.getWantedPosition().get(0).name());
        dto.setSubPos(user.getWantedPosition().get(1).name());
        return dto;
    }
}
