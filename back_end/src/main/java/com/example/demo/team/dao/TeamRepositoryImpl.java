package com.example.demo.team.dao;

import com.example.demo.ai.dto.CandidateDto;
import com.example.demo.ai.dto.TeamAIDto;
import com.example.demo.team.entity.QTeam;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.entity.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<TeamAIDto> findAvailableTeams() {
        QTeam team = QTeam.team;
        QUser member = QUser.user;

        // 1단계: 팀 기본 정보 조회 (멤버 수 < 6인 팀만)
        List<TeamAIDto> teams = queryFactory
                .select(Projections.fields(TeamAIDto.class,
                        team.id.as("teamId"),
                        team.teamName,
                        team.memberWanted,
                        team.teamPreference.as("goals"),
                        team.teamVive.as("vives")))
                .from(team)
                .where(team.members.size().lt(6))
                .fetch();

        // 각 팀의 members 리스트 초기화
        teams.forEach(teamDto -> teamDto.setMembers(new ArrayList<>()));

        if (teams.isEmpty()) {
            return teams;
        }

        // 팀 ID로 매핑을 위한 Map 생성
        Map<Long, TeamAIDto> teamMap = teams.stream()
                .collect(Collectors.toMap(TeamAIDto::getTeamId, dto -> dto));

        // 2단계: 멤버 정보 조회
        List<Tuple> memberResults = queryFactory
                .select(member.team.id,
                        member.id,
                        member.userName,
                        member.wantedPosition,
                        member.projectGoal,
                        member.projectVive)
                .from(member)
                .where(member.team.id.in(teamMap.keySet()))
                .fetch();

        // 3단계: 멤버 정보를 팀에 매핑
        for (Tuple tuple : memberResults) {
            Long teamId = tuple.get(member.team.id);
            List<PositionEnum> positions = tuple.get(member.wantedPosition);

            // 안전한 CandidateDto 생성
            CandidateDto memberDto = CandidateDto.builder()
                    .userId(tuple.get(member.id))
                    .userName(tuple.get(member.userName))
                    .mainPos(positions != null && !positions.isEmpty() ?
                            positions.get(0).name() : null)
                    .subPos(positions != null && positions.size() > 1 ?
                            positions.get(1).name() : null)
                    .goals(tuple.get(member.projectGoal))
                    .vives(tuple.get(member.projectVive))
                    .build();

            teamMap.get(teamId).getMembers().add(memberDto);
        }
        return teams;
    }
}
