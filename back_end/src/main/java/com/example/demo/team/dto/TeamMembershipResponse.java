package com.example.demo.team.dto;

import com.example.demo.team.entity.RequestStatus;
import com.example.demo.team.entity.RequestType;
import com.example.demo.team.entity.TeamMembershipRequest;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMembershipResponse {
    private Long requestId;
    private Long userId;
    private String userName;
    private Long teamId;
    private RequestType requestType;
    private RequestStatus status;
    private String message;
    private LocalDateTime createdAt;

    // 엔티티 -> DTO 변환 메서드
    public static TeamMembershipResponse fromEntity(TeamMembershipRequest entity) {
        TeamMembershipResponse dto = new TeamMembershipResponse();
        dto.setRequestId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        dto.setUserName(entity.getUser().getUserName());
        dto.setTeamId(entity.getTeam().getId());
        dto.setRequestType(entity.getRequestType());
        dto.setStatus(entity.getStatus());
        dto.setMessage(entity.getMessage());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}