package com.example.demo.ai.controller;

import com.example.demo.ai.dto.PersonToTeamDto;
import com.example.demo.ai.dto.TeamToPersonDto;
import com.example.demo.ai.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIController {

    private final AIService aiService;

    @GetMapping("/forteam")
    public TeamToPersonDto forTeam(Long teamId) {
        return aiService.findTeamToPersonDtoById(teamId);
    }

    @GetMapping("/foruser")
    public PersonToTeamDto forUser(Long userId) {
        return aiService.findPersonToTeamDtoById(userId);
    }
}
