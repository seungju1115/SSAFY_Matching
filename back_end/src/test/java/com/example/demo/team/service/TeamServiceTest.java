package com.example.demo.team.service;

import com.example.demo.chat.dto.ChatRoomResponse;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.service.ChatRoomService;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.dto.*;
import com.example.demo.team.entity.Team;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatRoomService chatRoomService;

    @InjectMocks
    private TeamService teamService;

    private User user,user1,user2;
    private Team team;
    private TeamRequest teamRequest;
    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUserName("Alice");

        user2 = new User();
        user2.setId(2L);
        user2.setUserName("Bob");

        team = new Team();
        team.setId(1L);
        team.setTeamName("테스트 팀");
        team.setTeamDomain("DOMAINwebDesign");
        team.setMembers(List.of(user1, user2));

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

        team.setLeader(user);
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(1L);
        team.setChatRoom(chatRoom);

        teamRequest = new TeamRequest();
        teamRequest.setTeamId(1L);
        teamRequest.setTeamName("테스트 팀2");
        teamRequest.setLeaderId(1L);
        teamRequest.setTeamDomain("exampledomain"); // 최소 10자 이상
    }

    @BeforeAll
    static void globalSetUp(){

    }


    @Test
    @DisplayName("팀 생성 성공")
    void createTeam_성공() {
        user.setTeam(null);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> {
            Team savedTeam = invocation.getArgument(0);
            savedTeam.setId(1L);
            savedTeam.setChatRoom(new ChatRoom()); // 추가
            savedTeam.getChatRoom().setId(1L);
            return savedTeam;
        });
        ChatRoomResponse mockResponse = new ChatRoomResponse();
        mockResponse.setRoomId(1L);
        // 필요한 필드 세팅
        when(chatRoomService.createTeamChatRoom(any(ChatRoomRequest.class)))
                .thenReturn(mockResponse);

        TeamResponse response = teamService.createTeam(teamRequest);

        assertThat(response.getTeamName()).isEqualTo(teamRequest.getTeamName());
        assertThat(response.getLeaderId()).isEqualTo(user.getId());
        assertThat(response.getMemberCount()).isEqualTo(1);

        verify(userRepository, times(1)).findById(user.getId());
        verify(teamRepository, times(1)).save(any(Team.class));
        verify(chatRoomService, times(1)).createTeamChatRoom(any(ChatRoomRequest.class));
    }

    @Test
    @DisplayName("팀 생성 실패 - 유저를 찾을 수 없음")
    void createTeam_userNotFound() {
        // given
        when(userRepository.findById(teamRequest.getLeaderId()))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> teamService.createTeam(teamRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());

        verify(userRepository, times(1)).findById(teamRequest.getLeaderId());
        verify(teamRepository, never()).save(any());
    }

    @Test
    @DisplayName("팀 생성 실패 - 이미 다른 팀에 소속된 경우")
    void createTeam_userAlreadyHasTeam() {
        // given
        user.setTeam(new Team()); // 이미 팀 소속 상태
        when(userRepository.findById(teamRequest.getLeaderId()))
                .thenReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> teamService.createTeam(teamRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.USER_ALLREADY_HAS_TEAM.getMessage());

        verify(userRepository, times(1)).findById(teamRequest.getLeaderId());
        verify(teamRepository, never()).save(any());
    }

    @Test
    @DisplayName("모든 팀 조회 성공")
    void getAllTeams_성공() {
        when(teamRepository.findAll()).thenReturn(List.of(team));

        List<TeamResponse> allTeams = teamService.getAllTeams();

        assertThat(allTeams).hasSize(1);
        assertThat(allTeams.get(0).getTeamName()).isEqualTo(team.getTeamName());
    }

    @Test
    @DisplayName("팀 조건 조회 성공 - teamName과 leaderId 둘 다 필터링")
    void searchConditionTeam_성공() {
        // given
        User leader1 = new User();
        leader1.setId(100L);

        User leader2 = new User();
        leader2.setId(200L);

        Team team1 = new Team();
        team1.setId(1L);
        team1.setTeamName("테스트A");
        team1.setLeader(leader1);
        team1.setMembers(List.of(leader1));
        team1.setChatRoom(new ChatRoom()); // 추가
        team1.getChatRoom().setId(1L);

        Team team2 = new Team();
        team2.setId(2L);
        team2.setTeamName("샘플B");
        team2.setLeader(leader2);
        team2.setMembers(List.of(leader2));
        team2.setChatRoom(new ChatRoom()); // 추가
        team2.getChatRoom().setId(2L);

        when(teamRepository.findAll()).thenReturn(List.of(team1, team2));

        TeamSearchRequest request = new TeamSearchRequest();
        request.setTeamName("테스트"); // 이름에 "테스트"가 포함된 팀만
        request.setLeaderId(100L);    // 리더 ID가 100L인 팀만

        // when
        List<TeamResponse> result = teamService.searchConditionTeam(request);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTeamName()).isEqualTo("테스트A");
        assertThat(result.get(0).getLeaderId()).isEqualTo(100L);

        verify(teamRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("팀 정보 조회 성공")
    void getTeam_성공() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        // when
        TeamDetailResponse response = teamService.getTeam(1L);

        // then
        assertThat(response.getTeamId()).isEqualTo(1L);
        assertThat(response.getTeamName()).isEqualTo("테스트 팀");
        assertThat(response.getLeaderId()).isEqualTo(1L);

        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("팀 정보조회 실패")
    void getTeam_존재하지_않는_팀() {
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.getTeam(999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.TEAM_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("팀 삭제 성공")
    void deleteTeam_성공() {
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        // 멤버가 team 참조 해제되는지 확인용
        // 멤버들의 team 참조 해제 (양방향 관계 주인: User.team)
        if (team.getMembers() != null) {
            for (User member : new ArrayList<>(team.getMembers())) {
                // member.setTeam(null); // 이 부분을 직접 컬렉션 수정 없이 null 할당으로 바꾸거나
                member.setTeam(null); // 이 호출 시 setTeam 내부에서 리스트 수정 안 하도록 해야 함
            }
        }
        teamService.deleteTeam(team.getId());

        // 멤버들의 team 필드가 null로 초기화됐는지 체크
        for (User member : team.getMembers()) {
            assertThat(member.getTeam()).isNull();
        }

        verify(teamRepository, times(1)).delete(team);
    }

    @Test
    @DisplayName("팀 삭제시 팀없음")
    void deleteTeam_팀없음_예외() {
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.deleteTeam(999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.TEAM_NOT_FOUND.getMessage());

        verify(teamRepository, never()).delete(any());
    }

    @Test
    @DisplayName("팀 수정 성공")
    void modifyTeam_성공() {
        teamRequest.setTeamName("수정된팀명");

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(teamRepository.findByTeamName("수정된팀명")).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        var response = teamService.modifyTeam(teamRequest);

        assertThat(response.getTeamName()).isEqualTo("수정된팀명");
        assertThat(response.getLeaderId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("팀명이 이미 존재")
    void modifyTeam_팀명_중복_예외() {
        teamRequest.setTeamName("기존팀명");

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(teamRepository.findByTeamName("기존팀명")).thenReturn(Optional.of(new Team()));

        assertThatThrownBy(() -> teamService.modifyTeam(teamRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.TEAM_NAME_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("팀 수정시 팀없음")
    void modifyTeam_팀없음_예외() {
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());
        teamRequest.setTeamId(999L);
        assertThatThrownBy(() -> teamService.modifyTeam(teamRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.TEAM_NOT_FOUND.getMessage());

        verify(teamRepository, never()).delete(any());
    }

    @Test
    @DisplayName("팀 수정시 유저없음")
    void modifyTeam_유저없음_예외() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        teamRequest.setLeaderId(999L);
        assertThatThrownBy(() -> teamService.modifyTeam(teamRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());

        verify(teamRepository, never()).delete(any());
    }

    @Test
    @DisplayName("팀원으로 추가 성공")
    void inviteMemberTeam_성공() {
        user.setTeam(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        doNothing().when(chatRoomService).addMemberToTeamChatRoom(any());

        var request = new TeamInviteRequest();
        request.setTeamId(team.getId());
        request.setUserId(1L);

        var response = teamService.inviteMemberTeam(request);

        assertThat(response.getMembersId()).contains(user.getId());
        assertThat(user.getTeam()).isEqualTo(team);

        verify(chatRoomService, times(1)).addMemberToTeamChatRoom(any());
    }

    @Test
    @DisplayName("팀원으로 등록된 멤버")
    void inviteMemberTeam_이미_팀_소속_예외() {
        user.setTeam(team);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        var request = new TeamInviteRequest();
        request.setTeamId(team.getId());
        request.setUserId(1L);

        assertThatThrownBy(() -> teamService.inviteMemberTeam(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.USER_ALLREADY_HAS_TEAM.getMessage());

        verify(chatRoomService, never()).addMemberToTeamChatRoom(any());
    }

    @Test
    @DisplayName("팀원으로 등록 시 팀 없음 예외")
    void inviteMemberTeam_팀_없음_예외() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        var request = new TeamInviteRequest();
        request.setTeamId(999L);
        request.setUserId(1L);

        assertThatThrownBy(() -> teamService.inviteMemberTeam(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.TEAM_NOT_FOUND.getMessage());

        verify(chatRoomService, never()).addMemberToTeamChatRoom(any());
    }

    @Test
    @DisplayName("팀원으로 등록 시 유저 찾을 수 없음")
    void inviteMemberTeam_유저_없음_예외() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        var request = new TeamInviteRequest();
        request.setUserId(999L);

        assertThatThrownBy(() -> teamService.inviteMemberTeam(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());

        verify(chatRoomService, never()).addMemberToTeamChatRoom(any());
    }

    @Test
    @DisplayName("팀 멤버 조회 성공")
    void getTeamMembers_success() {
        // given
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        // when
        List<TeamMemberResponse> result = teamService.getTeamMembers(1L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getMemberId()).isEqualTo(1L);
        assertThat(result.get(0).getUserName()).isEqualTo("Alice");
        assertThat(result.get(1).getMemberId()).isEqualTo(2L);
        assertThat(result.get(1).getUserName()).isEqualTo("Bob");

        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("팀 멤버 조회 실패 - 팀 없음")
    void getTeamMembers_teamNotFound() {
        // given
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> teamService.getTeamMembers(999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.TEAM_NOT_FOUND.getMessage());

        verify(teamRepository, times(1)).findById(999L);
    }
}
