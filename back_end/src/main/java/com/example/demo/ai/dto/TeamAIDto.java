package com.example.demo.ai.dto;

import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TeamAIDto {
    private Long teamId;
    private String teamName;
    private List<String> memberWanted;
    private Set<ProjectGoalEnum> goals;
    private Set<ProjectViveEnum> vives;
    private List<CandidateDto> members;

    @QueryProjection
    public TeamAIDto(Long teamId, String teamName, String memberWanted) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.memberWanted= Arrays.stream(memberWanted.split(",")).toList();
        this.members = new ArrayList<>();
    }

    public static TeamAIDto from(Team team) {
        TeamAIDto teamAIDto = new TeamAIDto();
        teamAIDto.teamId = team.getId();
        teamAIDto.teamName = team.getTeamName();
        teamAIDto.memberWanted = Arrays.stream(team.getMemberWanted().split(",")).toList();
        teamAIDto.goals = team.getTeamPreference();
        teamAIDto.vives = team.getTeamVive();
        teamAIDto.members = new ArrayList<>();
        for (User user : team.getMembers()) {
            teamAIDto.members.add(CandidateDto.from(user));
        }
        return teamAIDto;
    }
}
