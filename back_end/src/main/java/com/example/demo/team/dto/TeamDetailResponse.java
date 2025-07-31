package com.example.demo.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TeamDetailResponse implements Serializable {
    private Long teamId;
    private String teamName;
    private Long leaderId;
    private List<Long> membersId; // 팀에 소속된 인원 수
}
