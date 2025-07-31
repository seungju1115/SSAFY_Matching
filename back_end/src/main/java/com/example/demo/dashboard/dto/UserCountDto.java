package com.example.demo.dashboard.dto;

import com.example.demo.team.entity.Team;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCountDto {
    private Team team;
    private boolean major;
}
