package com.example.demo.team.dao;

import com.example.demo.ai.dto.TeamAIDto;

import java.util.List;

public interface TeamRepositoryCustom {
    List<TeamAIDto> findAvailableTeams();
}
