package com.example.demo.ai.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TeamToPersonDto {
    private TeamAIDto currentTeam;
    private List<CandidateDto> candidates;
}
