package com.example.demo.team.dto;

import com.example.demo.team.entity.TeamLockRequest;
import com.example.demo.user.dto.UserDetailResponse;
import com.example.demo.team.entity.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamLockResponse {

    private Long id;

    private Long teamId;

    private RequestStatus status;

    private String message;

    private LocalDateTime createdAt;

    // Entity -> DTO 변환 메서드
    public static TeamLockResponse fromEntity(TeamLockRequest entity) {
        return TeamLockResponse.builder()
                .id(entity.getId())
                 .teamId(entity.getTeam().getId())
                .status(entity.getStatus())
                .message(entity.getMessage())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
