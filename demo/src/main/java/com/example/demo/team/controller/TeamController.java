package com.example.demo.team.controller;

import com.example.demo.response.ApiResponse;
import com.example.demo.team.dto.*;
import com.example.demo.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    // 1. 팀 생성
    @PostMapping
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(@RequestBody TeamRequest teamRequest) {
        TeamResponse teamResponse = teamService.createTeam(teamRequest);
        return ResponseEntity.ok(ApiResponse.created(teamResponse));
    }

    // 2. 전체 팀 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getAllTeams() {
        List<TeamResponse> teamResponses = teamService.getAllTeams();
        return ResponseEntity.ok(ApiResponse.ok(teamResponses));
    }

    // 3. 팀 조건 조회
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> searchConditionTeam(@RequestBody TeamRequest teamRequest) {
        List<TeamResponse> teamResponses = teamService.searchConditionTeam(teamRequest);
        return ResponseEntity.ok(ApiResponse.ok(teamResponses));
    }

    // 4. 팀 정보 조회
    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamDetailResponse>> getTeam(@PathVariable Long teamId) {
        TeamDetailResponse teamDetailResponse = teamService.getTeam(teamId);
        return ResponseEntity.ok(ApiResponse.ok(teamDetailResponse));
    }

    // 5. 팀 정보 삭제
    @DeleteMapping("/{teamId}")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    // 6. 팀 정보 수정
    @PutMapping
    public ResponseEntity<ApiResponse<TeamDetailResponse>> modifyTeam(@RequestBody TeamRequest teamRequest) {
        TeamDetailResponse teamDetailResponse = teamService.modifyTeam(teamRequest);
        return ResponseEntity.ok(ApiResponse.ok(teamDetailResponse));
    }

    // 7. 팀 멤버 초대
    @GetMapping("/invitation")
    public ResponseEntity<ApiResponse<TeamDetailResponse>> inviteMemberTeam(@RequestBody InviteRequest inviteRequest) {
        TeamDetailResponse teamDetailResponse = teamService.inviteMemberTeam(inviteRequest);
        return ResponseEntity.ok(ApiResponse.ok(teamDetailResponse));
    }

    // n. 특정 팀의 팀원 조회
    @GetMapping("/{teamId}/members")
    public List<TeamMemberResponse> getTeamMembers(@PathVariable Long teamId) {
        return teamService.getTeamMembers(teamId);
    }
}