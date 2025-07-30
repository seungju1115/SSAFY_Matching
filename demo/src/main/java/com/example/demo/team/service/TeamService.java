package com.example.demo.team.service;

import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.entity.RoomType;
import com.example.demo.chat.service.ChatRoomService;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.dto.*;
import com.example.demo.team.entity.Team;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;

    // 1. 팀 생성
    @Transactional
    public TeamResponse createTeam(TeamRequest dto) {
        // 팀장 조회
        User leader = userRepository.findById(dto.getLeaderId())
                .orElseThrow(() -> new RuntimeException("Leader not found"));

        // 팀 객체 생성
        Team team = new Team();
        team.setTeamName(dto.getTeamName());
        team.setLeader(leader);

        // 팀장도 팀의 멤버로 설정 (양방향 메서드 이용)
        leader.setTeam(team); // 내부적으로 team.getMembers().add(this) 포함됨

        // 저장
        Team saved = teamRepository.save(team);

        ChatRoomRequest chatRoomRequest = new ChatRoomRequest();
        chatRoomRequest.setRoomType(RoomType.TEAM);
        chatRoomRequest.setTeamId(saved.getId());
        chatRoomRequest.setUserId(dto.getLeaderId());

        chatRoomService.createTeamChatRoom(chatRoomRequest);

        return new TeamResponse(saved.getId(), saved.getTeamName(), saved.getLeader().getId(), saved.getMembers().size());
    }

    // 2. 전체 팀 조회
    public List<TeamResponse> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(team -> new TeamResponse(
                        team.getId(),
                        team.getTeamName(),
                        team.getLeader() != null ? team.getLeader().getId() : null,
                        team.getMembers() != null ? team.getMembers().size() : 0
                ))
                .collect(Collectors.toList());
    }

    // 3. 팀 조건 조회
    public List<TeamResponse> searchConditionTeam(TeamRequest teamRequest) {
        return teamRepository.findAll().stream()
                .filter(team -> teamRequest.getTeamName() == null || team.getTeamName().contains(teamRequest.getTeamName()))
                .filter(team -> teamRequest.getLeaderId() == null ||
                        (team.getLeader() != null && team.getLeader().getId().equals(teamRequest.getLeaderId())))
                .map(team -> new TeamResponse(
                        team.getId(),
                        team.getTeamName(),
                        team.getLeader() != null ? team.getLeader().getId() : null,
                        team.getMembers() != null ? team.getMembers().size() : 0
                ))
                .collect(Collectors.toList());
    }

    // 4. 팀 정보 조회
    public TeamDetailResponse getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        List<Long> membersId = new ArrayList<>();
        for (User user : team.getMembers()) {
            membersId.add(user.getId());
        }

        return new TeamDetailResponse(team.getId(), team.getTeamName(), team.getLeader().getId(), membersId);
    }

    // 5. 팀 정보 삭제
    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

        // 멤버들의 team 참조 해제 (연관관계 주인: User.team)
        if (team.getMembers() != null) {
            for (User member : new ArrayList<>(team.getMembers())) {
                member.setTeam(null); // 안전하게 순회
            }
        }

        // 양방향 관계 정리 (옵션)
        if (team.getChatRoom() != null) {
            team.setChatRoom(null); // 양방향 동기화용
        }

        teamRepository.delete(team);
    }

    // 6. 팀 정보 수정
    @Transactional
    public TeamDetailResponse modifyTeam(TeamRequest teamRequest) {
        Team team = teamRepository.findById(teamRequest.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
        // 팀 이름 수정
        if (teamRequest.getTeamName() != null && !teamRequest.getTeamName().isBlank()) {
            team.setTeamName(teamRequest.getTeamName());
        }

        // 리더 수정
        if (teamRequest.getLeaderId() != null) {
            User newLeader = userRepository.findById(teamRequest.getLeaderId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
            team.setLeader(newLeader);
        }

        List<Long> membersId = new ArrayList<>();
        for (User user : team.getMembers()) {
            membersId.add(user.getId());
        }

        return new TeamDetailResponse(team.getId(), team.getTeamName(), team.getLeader().getId(), membersId);
    }

    // 7. 팀 멤버 초대
    public TeamDetailResponse inviteMemberTeam(InviteRequest inviteRequest) {
        User invitedUser = userRepository.findById(inviteRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Team team = teamRepository.findById(inviteRequest.getTeamId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 이미 팀에 소속된 사용자라면 예외 처리
        if (invitedUser.getTeam() != null && invitedUser.getTeam().getId().equals(team.getId())) {
            throw new RuntimeException("해당 사용자는 이미 이 팀에 속해 있습니다.");
        }

        invitedUser.setTeam(team);

        List<Long> membersId = new ArrayList<>();
        for (User user : team.getMembers()) {
            membersId.add(user.getId());
        }

        return new TeamDetailResponse(team.getId(), team.getTeamName(), team.getLeader().getId(), membersId);
    }

    // n. 팀 멤버 조회
    public List<TeamMemberResponse> getTeamMembers(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        List<User> members = team.getMembers();

        return members.stream()
                .map(user -> new TeamMemberResponse(user.getId(), user.getUserName()))
                .collect(Collectors.toList());
    }
}
