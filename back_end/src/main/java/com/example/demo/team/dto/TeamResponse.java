package com.example.demo.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamResponse {
    private Long teamId;
    private String teamName;
    private Long leaderId;
    private int memberCount; // 팀에 소속된 인원 수
}
