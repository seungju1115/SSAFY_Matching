package com.example.demo.team.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamRequest {
    private Long teamId;
    private String teamName;   // 팀 이름 (방 제목)
    private Long leaderId;     // 팀장(User)의 ID
}
