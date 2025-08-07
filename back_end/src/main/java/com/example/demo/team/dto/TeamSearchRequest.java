package com.example.demo.team.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamSearchRequest {

    @Size(min = 2, message = "팀 이름은 최소 2자 이상이어야 합니다.")
    private String teamName;
    private Long leaderId;
}
