package com.example.demo.ai.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PersonToTeamDto {
    private CandidateDto person;
    private List<TeamAIDto> teams;
}
