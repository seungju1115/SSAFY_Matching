package com.example.demo.ai.dto;

import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
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

    @QueryProjection
    public CandidateDto(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public static CandidateDto from(User user) {
        CandidateDto dto=new CandidateDto();
        dto.setUserId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setGoals(user.getProjectGoal());
        dto.setVives(user.getProjectVive());
        if(user.getWantedPosition().size()>0) dto.setMainPos(user.getWantedPosition().get(0).name());
        if(user.getWantedPosition().size()>1) dto.setSubPos(user.getWantedPosition().get(1).name());
        return dto;
    }
}
