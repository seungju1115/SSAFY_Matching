package com.example.demo.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamMemberResponse {
    private Long memberId;
    private String username;
}