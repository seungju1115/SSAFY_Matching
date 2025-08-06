package com.example.demo.team.service;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.team.dao.TeamMembershipRequestRepository;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.dto.TeamMemberResponse;
import com.example.demo.team.dto.TeamOffer;
import com.example.demo.team.entity.RequestStatus;
import com.example.demo.team.entity.RequestType;
import com.example.demo.team.entity.Team;
import com.example.demo.team.entity.TeamMembershipRequest;
import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TeamMembershipRequestServiceTest {

    @Mock
    private TeamMembershipRequestRepository teamMembershipRequestRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private TeamMembershipRequestService teamMembershipRequestService;

    private Team team;
    private User user;
    private TeamOffer teamOffer;
    private TeamMembershipRequest existingRequest;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setId(1L);
        team.setTeamName("teamA");
        team.setTeamDomain("DOMAINwebDesign");
        team.setMembershipRequests(new ArrayList<>());

        user = new User();
        user.setUserName("test");
        user.setRole("ROLE_USER");
        user.setWantedPosition(PositionEnum.BACKEND);
        user.setTechStack(Set.of(TechEnum.Docker));
        user.setPersonalPref(Set.of(PersonalPrefEnum.CONCENTRATE));
        user.setProjectPref(Set.of(ProjectPrefEnum.CHALLENGE));
        user.setLastClass(13);
        user.setEmail("test@gmail.com");
        user.setId(1L);

        teamOffer = new TeamOffer();
        teamOffer.setTeamId(1L);
        teamOffer.setUserId(1L);
        teamOffer.setRequestType(RequestType.INVITE);
        teamOffer.setMessage("초대 메시지");

        existingRequest = new TeamMembershipRequest();
        existingRequest.setUser(user);
        existingRequest.setRequestType(RequestType.INVITE);
        existingRequest.setStatus(RequestStatus.PENDING);
    }

    @BeforeAll
    static void globalSetUp(){

    }
    @Test
    @DisplayName("팀 -> 멤버 요청 성공")
    void requestTeamToMember_success() {
        // given
        when(teamRepository.findById(teamOffer.getTeamId())).thenReturn(Optional.of(team));
        when(userRepository.findById(teamOffer.getUserId())).thenReturn(Optional.of(user));
        when(teamMembershipRequestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        teamMembershipRequestService.requestTeamToMember(teamOffer);

        // then
        verify(teamMembershipRequestRepository, times(1)).save(any(TeamMembershipRequest.class));
        verify(messagingTemplate, times(1)).convertAndSend("/queue/team/offer/" + teamOffer.getUserId(), teamOffer.getMessage());
    }

    @Test
    @DisplayName("팀 -> 멤버 요청 실패 - 팀 없음")
    void requestTeamToMember_teamNotFound() {
        when(teamRepository.findById(teamOffer.getTeamId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamMembershipRequestService.requestTeamToMember(teamOffer))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.TEAM_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("팀 -> 멤버 요청 실패 - 유저 없음")
    void requestTeamToMember_userNotFound() {
        when(teamRepository.findById(teamOffer.getTeamId())).thenReturn(Optional.of(team));
        when(userRepository.findById(teamOffer.getUserId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamMembershipRequestService.requestTeamToMember(teamOffer))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("팀 -> 멤버 요청 실패 - 이미 초대 요청 존재")
    void requestTeamToMember_duplicateRequest() {
        team.setMembershipRequests(List.of(existingRequest));

        when(teamRepository.findById(teamOffer.getTeamId())).thenReturn(Optional.of(team));
        when(userRepository.findById(teamOffer.getUserId())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> teamMembershipRequestService.requestTeamToMember(teamOffer))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.TEAM_REQUEST_ALLREADY_EXIST.getMessage());
    }

    @Test
    @DisplayName("유저 -> 팀 요청 성공")
    void requestMemberToTeam_success() {
        teamOffer.setRequestType(RequestType.JOIN_REQUEST);
        user.setMembershipRequests(new ArrayList<>());
        when(teamRepository.findById(teamOffer.getTeamId())).thenReturn(Optional.of(team));
        when(userRepository.findById(teamOffer.getUserId())).thenReturn(Optional.of(user));
        when(teamMembershipRequestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // 팀 멤버 리스트 준비
        var memberResponse1 = new TeamMemberResponse();
        memberResponse1.setMemberId(100L);
        var memberResponse2 = new TeamMemberResponse();
        memberResponse2.setMemberId(200L);

        when(teamService.getTeamMembers(teamOffer.getTeamId())).thenReturn(List.of(memberResponse1, memberResponse2));

        teamMembershipRequestService.requestMemberToTeam(teamOffer);

        verify(teamMembershipRequestRepository, times(1)).save(any(TeamMembershipRequest.class));
        verify(messagingTemplate, times(1)).convertAndSend("/queue/team/offer/100", teamOffer.getMessage());
        verify(messagingTemplate, times(1)).convertAndSend("/queue/team/offer/200", teamOffer.getMessage());
    }

    @Test
    @DisplayName("유저 -> 팀 요청 실패 - 팀 없음")
    void requestMemberToTeam_teamNotFound() {
        when(teamRepository.findById(teamOffer.getTeamId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamMembershipRequestService.requestTeamToMember(teamOffer))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.TEAM_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("유저 -> 팀 요청 실패 - 유저 없음")
    void requestMemberToTeam_userNotFound() {
        when(teamRepository.findById(teamOffer.getTeamId())).thenReturn(Optional.of(team));
        when(userRepository.findById(teamOffer.getUserId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamMembershipRequestService.requestTeamToMember(teamOffer))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("유저 -> 팀 요청 실패 - 이미 초대 요청 존재")
    void requestMemberToTeam_duplicateRequest() {
        team.setMembershipRequests(List.of(existingRequest));

        when(teamRepository.findById(teamOffer.getTeamId())).thenReturn(Optional.of(team));
        when(userRepository.findById(teamOffer.getUserId())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> teamMembershipRequestService.requestTeamToMember(teamOffer))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.TEAM_REQUEST_ALLREADY_EXIST.getMessage());
    }

    @Test
    @DisplayName("saveTeamOffer가 올바른 데이터로 호출되는지 검증")
    void saveTeamOffer_shouldSaveWithCorrectValues() {
        // given
        teamOffer.setRequestType(RequestType.JOIN_REQUEST);
        user.setMembershipRequests(new ArrayList<>());

        when(teamRepository.findById(teamOffer.getTeamId())).thenReturn(Optional.of(team));
        when(userRepository.findById(teamOffer.getUserId())).thenReturn(Optional.of(user));
        when(teamMembershipRequestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        when(teamService.getTeamMembers(teamOffer.getTeamId())).thenReturn(List.of());

        // when
        teamMembershipRequestService.requestMemberToTeam(teamOffer);

        // then
        ArgumentCaptor<TeamMembershipRequest> captor = ArgumentCaptor.forClass(TeamMembershipRequest.class);
        verify(teamMembershipRequestRepository).save(captor.capture());

        TeamMembershipRequest saved = captor.getValue();
        assertThat(saved.getTeam()).isEqualTo(team);
        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getRequestType()).isEqualTo(RequestType.JOIN_REQUEST);
        assertThat(saved.getStatus()).isEqualTo(RequestStatus.PENDING);
        assertThat(saved.getMessage()).isEqualTo("초대 메시지");
    }
}
