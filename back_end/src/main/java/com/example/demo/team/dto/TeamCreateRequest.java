package com.example.demo.team.dto;

import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Schema(description = "팀 생성 요청")
public class TeamCreateRequest {
    Long leaderId;
    String teamDomain;
    Set<ProjectViveEnum> teamVive;
    Set<ProjectGoalEnum> teamPreference;
    private int backendCount;
    private int frontendCount;
    private int aiCount;
    private int pmCount;
    private int designCount;
    String teamDescription;

    public static Team toTeam(TeamCreateRequest request, User leader) {
        Team team = new Team();
        
        // 역할별 인원 수 설정
        team.setBackendCount(request.backendCount);
        team.setFrontendCount(request.frontendCount);
        team.setAiCount(request.aiCount);
        team.setPmCount(request.pmCount);
        team.setDesignCount(request.designCount);
        
        // 기본 팀 정보 설정
        team.setTeamDomain(request.teamDomain);
        team.setTeamDescription(request.teamDescription);
        team.setTeamPreference(request.teamPreference);
        team.setTeamVive(request.teamVive);
        
        // 팀장 설정
        team.setLeader(leader);
        
        return team;
    }


}
