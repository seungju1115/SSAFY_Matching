package com.example.demo.team.service;

import com.example.demo.chat.dto.ChatRoomResponse;
import com.example.demo.chat.service.ChatRoomService;
import com.example.demo.team.dto.TeamMemberResponse;
import com.example.demo.team.dto.TeamRequest;
import com.example.demo.team.dto.TeamResponse;
import com.example.demo.team.entity.Team;
import com.example.demo.user.entity.User;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.team.dao.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.team.dto.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

//@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatRoomService chatRoomService;

    private User leader;
    private Team team;

    @BeforeEach
    void setUp() {
        leader = new User();
        leader.setId(1L);
        leader.setUserName("leaderUser");
        leader.setTeam(null);

        team = new Team();
        team.setId(10L);
        team.setTeamName("TeamA");
        team.setLeader(leader);
        team.setMembers(new ArrayList<>());
    }

    @Test
    void createTeam_success() {
        TeamRequest request = new TeamRequest();
        request.setLeaderId(1L);
        request.setTeamName("TeamA");

        when(userRepository.findById(1L)).thenReturn(Optional.of(leader));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> {
            Team arg = invocation.getArgument(0);
            arg.setId(10L);
            return arg;
        });

        // ✅ ChatRoomService의 createTeamChatRoom 메서드가 ChatRoomResponse를 반환하도록 Mock 설정
        ChatRoomResponse mockChatRoomResponse = new ChatRoomResponse();
        when(chatRoomService.createTeamChatRoom(any(ChatRoomRequest.class)))
                .thenReturn(mockChatRoomResponse);

        TeamResponse response = teamService.createTeam(request);

        assertThat(response).isNotNull();
        assertThat(response.getTeamId()).isEqualTo(10L);
        assertThat(response.getTeamName()).isEqualTo("TeamA");
        assertThat(response.getLeaderId()).isEqualTo(1L);
        assertThat(response.getMemberCount()).isEqualTo(1); // leader 도 멤버에 포함

        verify(userRepository).findById(1L);
        verify(teamRepository).save(any(Team.class));
        verify(chatRoomService).createTeamChatRoom(any(ChatRoomRequest.class));
    }

    @Test
    void getAllTeams_returnsList() {
        team.getMembers().add(leader);
        when(teamRepository.findAll()).thenReturn(List.of(team));

        List<TeamResponse> list = teamService.getAllTeams();

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getTeamId()).isEqualTo(team.getId());
        assertThat(list.get(0).getMemberCount()).isEqualTo(1);
    }

    @Test
    void searchConditionTeam_filtersCorrectly() {
        team.getMembers().add(leader);

        when(teamRepository.findAll()).thenReturn(List.of(team));

        TeamRequest req = new TeamRequest();
        req.setTeamName("Team");
        req.setLeaderId(1L);

        List<TeamResponse> results = teamService.searchConditionTeam(req);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTeamName()).contains("Team");
        assertThat(results.get(0).getLeaderId()).isEqualTo(1L);
    }

    @Test
    void getTeam_returnsTeamDetailResponse() {
        team.getMembers().add(leader);
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));

        TeamDetailResponse detail = teamService.getTeam(10L);

        assertThat(detail).isNotNull();
        assertThat(detail.getTeamId()).isEqualTo(10L);
        assertThat(detail.getLeaderId()).isEqualTo(1L);
        assertThat(detail.getMembersId()).contains(leader.getId());
    }

    @Test
    void deleteTeam_removesTeamAndUnsetsMembers() {
        User member = new User();
        member.setId(2L);
        member.setTeam(team);

        team.getMembers().add(member);
        team.getMembers().add(leader);

        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        doNothing().when(teamRepository).delete(team);

        teamService.deleteTeam(10L);

        assertThat(member.getTeam()).isNull();
        assertThat(leader.getTeam()).isNull();

        verify(teamRepository).delete(team);
    }

    @Test
    void modifyTeam_updatesNameAndLeader() {
        User newLeader = new User();
        newLeader.setId(3L);

        team.getMembers().add(leader);

        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(userRepository.findById(3L)).thenReturn(Optional.of(newLeader));

        TeamRequest req = new TeamRequest();
        req.setTeamId(10L);
        req.setTeamName("NewName");
        req.setLeaderId(3L);

        TeamDetailResponse response = teamService.modifyTeam(req);

        assertThat(response.getTeamName()).isEqualTo("NewName");
        assertThat(response.getLeaderId()).isEqualTo(3L);
    }

    @Test
    void inviteMemberTeam_addsUserToTeam() {
        User newUser = new User();
        newUser.setId(4L);

        team.getMembers().add(leader);

        when(userRepository.findById(4L)).thenReturn(Optional.of(newUser));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));

        InviteRequest inviteRequest = new InviteRequest();
        inviteRequest.setUserId(4L);
        inviteRequest.setTeamId(10L);

        TeamDetailResponse response = teamService.inviteMemberTeam(inviteRequest);

        assertThat(response.getMembersId()).contains(leader.getId(), newUser.getId());
        assertThat(newUser.getTeam()).isEqualTo(team);
    }

    @Test
    void getTeamMembers_returnsMemberList() {
        team.getMembers().add(leader);

        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));

        List<TeamMemberResponse> members = teamService.getTeamMembers(10L);

        assertThat(members).hasSize(1);
        assertThat(members.get(0).getMemberId()).isEqualTo(leader.getId());
        assertThat(members.get(0).getUsername()).isEqualTo(leader.getUserName());
    }
}

