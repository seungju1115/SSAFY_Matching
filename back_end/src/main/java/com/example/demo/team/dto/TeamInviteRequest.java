package com.example.demo.team.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamInviteRequest {
    private Long userId;
    private Long teamId;
}
