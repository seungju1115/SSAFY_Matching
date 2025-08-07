package com.example.demo.ai.service;

import com.example.demo.ai.dto.CandidateDto;
import com.example.demo.ai.dto.PersonToTeamDto;
import com.example.demo.ai.dto.TeamAIDto;
import com.example.demo.ai.dto.TeamToPersonDto;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.entity.Team;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AIService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public TeamToPersonDto findTeamToPersonDtoById(Long teamId){
        TeamToPersonDto teamToPersonDto=new TeamToPersonDto();
        TeamAIDto curTeam=TeamAIDto.from(teamRepository.findTeamAIDtoById(teamId));
        List<CandidateDto> candidateDtos=new ArrayList<>();
        List<User> list=userRepository.findAllCandidates(teamId);
        for(User user:list){
            candidateDtos.add(CandidateDto.from(user));
        }
        teamToPersonDto.setCurrentTeam(curTeam);
        teamToPersonDto.setCandidates(candidateDtos);
        return teamToPersonDto;
    }

    public PersonToTeamDto findPersonToTeamDtoById(Long personId){
        PersonToTeamDto personToTeamDto=new PersonToTeamDto();
        CandidateDto curPerson=CandidateDto.from(userRepository.findCurUser(personId));
        List<TeamAIDto> availableTeams=new ArrayList<>();
        List<Team> list=teamRepository.findAvailableTeams();
        for(Team team:list){
            availableTeams.add(TeamAIDto.from(team));
        }
        personToTeamDto.setPerson(curPerson);
        personToTeamDto.setTeams(availableTeams);
        return personToTeamDto;
    }
}
