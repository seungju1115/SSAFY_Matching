package com.example.demo.team.service;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.team.dao.TeamMembershipRequestRepository;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.dto.TeamOffer;
import com.example.demo.team.entity.RequestStatus;
import com.example.demo.team.entity.RequestType;
import com.example.demo.team.entity.Team;
import com.example.demo.team.entity.TeamMembershipRequest;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.CPSubsystem;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
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

    @Mock
    private HazelcastInstance hazelcastInstance; // 1. HazelcastInstance를 Mock으로 선언

    @Mock
    private FencedLock fencedLock; // 2. FencedLock도 Mock으로 선언

    @Mock
    private IMap<Object, Object> lock;

    @Mock
    private CPSubsystem cpSubsystem; // 3. getCPSubsystem()이 반환할 객체도 Mock으로 선언

    private Team team;
    private User user,user2;
    private TeamOffer teamOffer;
    private TeamMembershipRequest existingRequest;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setId(1L);
        team.setTeamName("teamA");
        team.setTeamDomain("DOMAINwebDesign");
        team.setMembershipRequests(new ArrayList<>());
        team.setMembers(new ArrayList<>());

        user = new User();
        user.setUserName("test");
        user.setRole("ROLE_USER");
        user.setWantedPosition(new ArrayList<>(Arrays.asList(PositionEnum.DESIGN)));
        user.setTechStack(new HashSet<>(Arrays.asList(TechEnum.JPA, TechEnum.JPA)));
        user.setProjectGoal(new HashSet<>(Arrays.asList(ProjectGoalEnum.AWARD)));
        user.setProjectVive(new HashSet<>(Arrays.asList(ProjectViveEnum.AGILE)));
        user.setLastClass(13);
        user.setEmail("test@gmail.com");
        user.setId(1L);

        user2 = new User();
        user2.setUserName("test");
        user2.setRole("ROLE_USER");
        user2.setWantedPosition(new ArrayList<>(Arrays.asList(PositionEnum.DESIGN)));
        user2.setTechStack(new HashSet<>(Arrays.asList(TechEnum.JPA, TechEnum.JPA)));
        user2.setProjectGoal(new HashSet<>(Arrays.asList(ProjectGoalEnum.AWARD)));
        user2.setProjectVive(new HashSet<>(Arrays.asList(ProjectViveEnum.AGILE)));
        user2.setLastClass(13);
        user2.setEmail("test@gmail.com");
        user2.setId(2L);

        team.getMembers().add(user);
        team.getMembers().add(user2);

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

    @Test
    @DisplayName("팀 -> 멤버 요청 성공")
    void requestTeamToMember_success() {
        when(hazelcastInstance.getMap("teamMembershipRequestLock")).thenReturn(lock);
        //try {
        //    doReturn(true).when(lock).tryLock(anyString(), anyLong(), any(TimeUnit.class));
        //} catch (InterruptedException e) {
        //    throw new RuntimeException(e);
       // }
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
        when(hazelcastInstance.getMap("teamMembershipRequestLock")).thenReturn(lock);
        //try {
       //     doReturn(true).when(lock).tryLock(anyString(), anyLong(), any(TimeUnit.class));
       // } catch (InterruptedException e) {
       //     throw new RuntimeException(e);
       // }

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
        when(hazelcastInstance.getMap("teamMembershipRequestLock")).thenReturn(lock);
       // try {
            //doReturn(true).when(lock).tryLock(anyString(), anyLong(), any(TimeUnit.class));
        //} catch (InterruptedException e) {
         //   throw new RuntimeException(e);
       // }

        when(teamRepository.findById(teamOffer.getTeamId())).thenReturn(Optional.of(team));
        when(userRepository.findById(teamOffer.getUserId())).thenReturn(Optional.of(user));
        when(teamMembershipRequestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        teamMembershipRequestService.requestMemberToTeam(teamOffer);

        verify(teamMembershipRequestRepository, times(1)).save(any(TeamMembershipRequest.class));
        verify(messagingTemplate, times(1)).convertAndSend("/queue/team/offer/"+team.getMembers().get(0).getId(), teamOffer.getMessage());
        verify(messagingTemplate, times(1)).convertAndSend("/queue/team/offer/"+team.getMembers().get(0).getId(), teamOffer.getMessage());
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

        when(hazelcastInstance.getMap("teamMembershipRequestLock")).thenReturn(lock);
       // try {
        //    doReturn(true).when(lock).tryLock(anyString(), anyLong(), any(TimeUnit.class));
        //} catch (InterruptedException e) {
        //   throw new RuntimeException(e);
        //}

        when(teamRepository.findById(teamOffer.getTeamId())).thenReturn(Optional.of(team));
        when(userRepository.findById(teamOffer.getUserId())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> teamMembershipRequestService.requestTeamToMember(teamOffer))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.TEAM_REQUEST_ALLREADY_EXIST.getMessage());
    }

    @Test
    @DisplayName("saveTeamOffer가 올바른 데이터로 호출되는지 검증")
    void saveTeamOffer_shouldSaveWithCorrectValues() {
        teamMembershipRequestService.saveTeamOffer(teamOffer, team, user);

        ArgumentCaptor<TeamMembershipRequest> requestCaptor = ArgumentCaptor.forClass(TeamMembershipRequest.class);

        // 2. teamMembershipRequestRepository의 save 메서드가 1번 호출되었는지,
        //    그리고 그때 전달된 인자를 캡처합니다.
        verify(teamMembershipRequestRepository, times(1)).save(requestCaptor.capture());

        // 3. 캡처된 객체를 가져옵니다.
        TeamMembershipRequest capturedRequest = requestCaptor.getValue();

        // 4. 캡처된 객체의 각 필드가 우리가 준비한 데이터와 일치하는지 검증합니다.
        assertThat(capturedRequest.getTeam()).isEqualTo(team);
        assertThat(capturedRequest.getUser()).isEqualTo(user);
        assertThat(capturedRequest.getRequestType()).isEqualTo(teamOffer.getRequestType());
        assertThat(capturedRequest.getStatus()).isEqualTo(RequestStatus.PENDING);
        assertThat(capturedRequest.getMessage()).isEqualTo(teamOffer.getMessage());
    }

}
