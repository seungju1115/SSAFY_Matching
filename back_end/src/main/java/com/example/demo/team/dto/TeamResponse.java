package com.example.demo.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponse {
    private Long teamId;
    private Long chatRoomId;
    private String teamName;
    private Long leaderId;
    private int memberCount; // 팀에 소속된 인원 수
}
