package com.example.demo.dashboard.dto;

import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.PositionEnum;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCountDto {
    private Team team;
    private boolean major;
    private PositionEnum position;
}
