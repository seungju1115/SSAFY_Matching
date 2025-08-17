package com.example.demo.team.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.team.dto.*;
import com.example.demo.team.entity.RequestType;
import com.example.demo.team.entity.TeamMembershipRequest;
import com.example.demo.team.service.TeamMembershipRequestService;
import com.example.demo.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamMembershipRequestService teamMembershipRequestService;
    // 1. 팀 생성
    @PostMapping
    public ResponseEntity<ApiResponse<TeamDetailResponse>> createTeam(@Valid @RequestBody TeamRequest teamRequest) {
        return ResponseEntity.ok(ApiResponse.created(teamService.createTeam(teamRequest)));
    }

    // 2. 전체 팀 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeamDetailResponse>>> getAllTeams() {
        return ResponseEntity.ok(ApiResponse.ok(teamService.getAllTeams()));
    }

    // 3. 팀 조건 조회
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<TeamDetailResponse>>> searchConditionTeam(@Valid @RequestBody TeamSearchRequest teamRequest) {
        return ResponseEntity.ok(ApiResponse.ok(teamService.searchConditionTeam(teamRequest)));
    }

    // 4. 팀 정보 조회
    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamDetailResponse>> getTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(ApiResponse.ok(teamService.getTeam(teamId)));
    }

    // 5. 팀 정보 삭제
    @DeleteMapping("/{teamId}")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    // 6. 팀 정보 수정
    @PutMapping
    public ResponseEntity<ApiResponse<TeamDetailResponse>> modifyTeam(@Valid @RequestBody TeamRequest teamRequest) {
        TeamDetailResponse teamDetailResponse = teamService.modifyTeam(teamRequest);
        return ResponseEntity.ok(ApiResponse.ok(teamDetailResponse));
    }

    // 7. 팀 멤버 초대
    @PostMapping("/invitation")
    public ResponseEntity<ApiResponse<Void>> inviteMemberTeam(@Valid @RequestBody TeamInviteRequest teamInviteRequest) {
        teamService.inviteMemberTeam(teamInviteRequest);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    // 8. 팀 멤버 초대 요청
    @PostMapping("/offer")
    public ResponseEntity<ApiResponse<Void>> teamOffer(@Valid @RequestBody TeamOffer teamOffer) {

        if (teamOffer.getRequestType() != RequestType.INVITE) {
            teamMembershipRequestService.requestMemberToTeam(teamOffer);
        }else{
            teamMembershipRequestService.requestTeamToMember(teamOffer);
        }

        return ResponseEntity.ok(ApiResponse.created(null));
    }

    @PostMapping("/{userId}/leave")
    public ResponseEntity<ApiResponse<Void>> leaveTeam(@PathVariable Long userId) {
        teamService.leaveTeam(userId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/{teamId}/request")
    public ResponseEntity<ApiResponse<List<TeamMembershipResponse>>> getAllTeamRequest(@PathVariable Long teamId) {
        return ResponseEntity.ok(ApiResponse.ok(teamMembershipRequestService.getAllTeamRequest(teamId)));
    }

    @GetMapping("/{userId}/request/user")
    public ResponseEntity<ApiResponse<List<TeamMembershipResponse>>> getAllUserRequest(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(teamMembershipRequestService.getAllUserRequest(userId)));
    }

    @PostMapping("/{teamId}/lock")
    public ResponseEntity<ApiResponse<Void>> lockTeam(@PathVariable Long teamId) {
        teamService.lockTeam(teamId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

}
