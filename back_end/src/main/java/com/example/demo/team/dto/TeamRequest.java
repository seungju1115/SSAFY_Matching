package com.example.demo.team.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamRequest {
    private Long teamId;
    @NotBlank(message = "팀 이름은 필수입니다.")
    private String teamName;   // 팀 이름 (방 제목)

    @NotNull(message = "팀장 ID는 필수입니다.")
    private Long leaderId;     // 팀장(User)의 ID

    @NotNull(message = "도메인은 필수입니다.")
    @Size(min=10)
    private String teamDomain;
}