package com.example.demo.team.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamLockCreateRequest {

    private Long teamId;

    private String message;

}