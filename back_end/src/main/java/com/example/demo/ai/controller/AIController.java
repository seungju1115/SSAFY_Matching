package com.example.demo.ai.controller;

import com.example.demo.ai.dto.CandidateDto;
import com.example.demo.ai.dto.PersonToTeamDto;
import com.example.demo.ai.dto.TeamAIDto;
import com.example.demo.ai.dto.TeamToPersonDto;
import com.example.demo.ai.service.AIService;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIController {

    private final AIService aiService;


    @GetMapping("/recommend/candidates/{teamId}")
    public ResponseEntity<List<CandidateDto>> recommendCandidates(@PathVariable Long teamId) {
        List<CandidateDto> recommendations = aiService.recommendCandidatesForTeam(teamId, false);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/recommend/teams/{personId}")
    public ResponseEntity<List<TeamAIDto>> recommendTeams(@PathVariable Long personId) {
        List<TeamAIDto> recommendations = aiService.recommendTeamsForPerson(personId,false);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/recommend/candidates/{teamId}/all")
    public ResponseEntity<List<CandidateDto>> recommendCandidatesAll(@PathVariable Long teamId) {
        List<CandidateDto> recommendations = aiService.recommendCandidatesForTeam(teamId,true);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/recommend/teams/{personId}/all")
    public ResponseEntity<List<TeamAIDto>> recommendTeamsAll(@PathVariable Long personId) {
        List<TeamAIDto> recommendations = aiService.recommendTeamsForPerson(personId,true);
        return ResponseEntity.ok(recommendations);
    }

    // 기존 데이터만 가져오는 엔드포인트 (RecSys 호출 없이)
    @GetMapping("/data/team-to-person/{teamId}")
    public ResponseEntity<TeamToPersonDto> getTeamToPersonData(@PathVariable Long teamId) {
        TeamToPersonDto data = aiService.findTeamToPersonDtoById(teamId);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/data/person-to-team/{personId}")
    public ResponseEntity<PersonToTeamDto> getPersonToTeamData(@PathVariable Long personId) {
        PersonToTeamDto data = aiService.findPersonToTeamDtoById(personId);
        return ResponseEntity.ok(data);
    }
}
