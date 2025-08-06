package com.example.demo.team.service;

import com.example.demo.team.dao.TeamMembershipRequestRepository;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.dto.TeamOffer;
import com.example.demo.team.dto.TeamMemberResponse;
import com.example.demo.team.entity.RequestStatus;
import com.example.demo.team.entity.RequestType;
import com.example.demo.team.entity.Team;
import com.example.demo.team.entity.TeamMembershipRequest;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.CPSubsystem;
import com.hazelcast.cp.lock.FencedLock;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

class TeamMembershipRequestServiceTest {

    @InjectMocks
    TeamMembershipRequestService requestService;

    @Mock
    TeamMembershipRequestRepository requestRepository;

    @Mock
    TeamRepository teamRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    TeamService teamService;

    @Mock
    SimpMessagingTemplate messagingTemplate;

    @Mock
    HazelcastInstance hazelcastInstance;

    @Mock
    CPSubsystem cpSubsystem;

    @Mock
    FencedLock fencedLock;

    Team team;
    User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Hazelcast Mock 설정
        when(hazelcastInstance.getCPSubsystem()).thenReturn(cpSubsystem);
        when(cpSubsystem.getLock(anyString())).thenReturn(fencedLock);
        when(fencedLock.tryLock(anyLong(), any())).thenReturn(true);

        // 공통 데이터 세팅
        team = new Team();
        team.setId(1L);
        user = new User();
        user.setId(100L);
    }

    @Test
    void test_requestTeamToMember_정상동작() {
        // given
        TeamOffer offer = new TeamOffer(RequestType.INVITE, 100L,"팀에 초대합니다." , 1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(100L)).thenReturn(Optional.of(user));
        when(requestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        requestService.requestTeamToMember(offer);

        // then
        verify(requestRepository, times(1)).save(any(TeamMembershipRequest.class));
        verify(messagingTemplate).convertAndSend(eq("/queue/team/offer/100"), eq("팀에 초대합니다."));
    }

    @Test
    void test_requestMemberToTeam_정상동작() {
        // given
        TeamOffer offer = new TeamOffer(RequestType.JOIN_REQUEST, 100L,"가입 요청합니다." , 1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(100L)).thenReturn(Optional.of(user));
        when(requestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // 팀 멤버 응답 모킹
        TeamMemberResponse member1 = new TeamMemberResponse(200L, "팀장1");
        TeamMemberResponse member2 = new TeamMemberResponse(201L, "팀원2");
        when(teamService.getTeamMembers(1L)).thenReturn(Arrays.asList(member1, member2));

        // when
        requestService.requestMemberToTeam(offer);

        // then
        verify(requestRepository, times(1)).save(any(TeamMembershipRequest.class));
        verify(messagingTemplate).convertAndSend(eq("/queue/team/offer/200"), eq("가입 요청합니다."));
        verify(messagingTemplate).convertAndSend(eq("/queue/team/offer/201"), eq("가입 요청합니다."));
    }

    @Test
    void test_requestTeamToMember_중복요청_저장되지않음() {
        // given
        TeamOffer offer = new TeamOffer(RequestType.INVITE, 100L,"중복 요청입니다." , 1L);
        TeamMembershipRequest existingRequest = new TeamMembershipRequest();
        existingRequest.setRequestType(RequestType.INVITE);
        existingRequest.setStatus(RequestStatus.PENDING);
        existingRequest.setUser(user);
        existingRequest.setTeam(team);

        team.getMembershipRequests().add(existingRequest);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(100L)).thenReturn(Optional.of(user));

        // when
        requestService.requestTeamToMember(offer);

        // then
        verify(requestRepository, never()).save(any());
        verify(messagingTemplate, never()).convertAndSend(anyString(), anyString());
    }
}
