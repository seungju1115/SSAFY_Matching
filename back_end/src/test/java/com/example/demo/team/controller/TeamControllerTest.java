package com.example.demo.team.controller;

import com.example.demo.auth.filter.JwtFilter;
import com.example.demo.auth.util.JwtUtil;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.team.dto.*;
import com.example.demo.team.entity.RequestStatus;
import com.example.demo.team.entity.RequestType;
import com.example.demo.team.service.TeamMembershipRequestService;
import com.example.demo.team.service.TeamService;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.dto.UserDetailResponse;
import com.example.demo.user.dto.UserProfileResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private TeamMembershipRequestService teamMembershipRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtFilter jwtFilter; // JwtFilter 자체를 mock

    @MockitoBean
    private JwtUtil jwtUtil; // JwtUtil도 mock해버리면 해결됨

    private TeamRequest teamRequest;
    private TeamSearchRequest teamSearchRequest;
    private TeamOffer offer;
    private TeamInviteRequest inviteRequest;
    @BeforeEach
    void setUp(){
        teamRequest = new TeamRequest();
        teamRequest.setTeamId(1L);
        teamRequest.setTeamName("성장하는 A팀");
        teamRequest.setLeaderId(100L);
        teamRequest.setTeamDomain("growth-team-a"); // 최소 10자 이상
        teamRequest.setTeamVive(new HashSet<>(Arrays.asList(ProjectViveEnum.AGILE)));
        teamRequest.setTeamPreference(new HashSet<>(Arrays.asList(ProjectGoalEnum.IDEA)));
        teamRequest.setBackendCount(2);
        teamRequest.setFrontendCount(2);
        teamRequest.setAiCount(1);
        teamRequest.setPmCount(1);
        teamRequest.setDesignCount(0);
        teamRequest.setTeamDescription("함께 성장하며 멋진 포트폴리오를 만들고 싶습니다.");

        teamSearchRequest = new TeamSearchRequest();
        teamSearchRequest.setLeaderId(1L);
        teamSearchRequest.setTeamName("test");

        offer = new TeamOffer();
        offer.setRequestType(RequestType.JOIN_REQUEST); // INVITE가 아니면 requestMemberToTeam 호출
        offer.setTeamId(1L);
        offer.setUserId(10L);
        offer.setMessage("함께 합시다.");

        inviteRequest = new TeamInviteRequest();
        inviteRequest.setTeamId(1L);
        inviteRequest.setUserId(10L);
    }

    static private TeamDetailResponse teamDetailResponse1,teamDetailResponse2;
    static private UserDetailResponse userDetailResponse1,userDetailResponse2;
    static private List<TeamDetailResponse> teamList;
    @BeforeAll
    static void globalSetUp() {
        // --- 사용자 1: 백엔드 개발자 지망생 ---
        userDetailResponse1 = UserDetailResponse.builder()
                .id(100L)
                .userName("김백엔")
                .role("USER")
                .email("backend.kim@example.com")
                .userProfile("Spring Boot와 JPA에 자신 있는 백엔드 개발자입니다.")
                .major(true)
                .lastClass(8)
                .wantedPosition(new ArrayList<>(Arrays.asList(PositionEnum.DESIGN)))
                .projectGoal(Set.of(ProjectGoalEnum.AWARD, ProjectGoalEnum.PORTFOLIO))
                .projectVive(Set.of(ProjectViveEnum.RULE, ProjectViveEnum.AGILE))
                .projectExp("실시간 채팅 기능이 포함된 소셜 미디어 플랫폼 개발 경험")
                .qualification("SQLD")
                .techStack(Set.of(TechEnum.JAVA, TechEnum.SPRING, TechEnum.JPA, TechEnum.MYSQL))
                .build();

// --- 사용자 2: UI/UX 디자이너 ---
        userDetailResponse2 = UserDetailResponse.builder()
                .id(200L)
                .userName("이디자인")
                .role("USER")
                .email("design.lee@example.com")
                .userProfile("사용자 중심의 직관적인 UI/UX를 설계합니다.")
                .major(true)
                .lastClass(7)
                .wantedPosition(new ArrayList<>(Arrays.asList(PositionEnum.DESIGN)))
                .projectGoal(Set.of(ProjectGoalEnum.PORTFOLIO))
                .projectVive(Set.of(ProjectViveEnum.STABLE, ProjectViveEnum.CASUAL))
                .projectExp("모바일 앱 디자인 및 프로토타이핑 다수 경험")
                .qualification("GTQ 포토샵 1급")
                .techStack(Set.of(TechEnum.JAVA, TechEnum.ANSIBLE))
                .build();

// --- 첫 번째 팀 상세 정보 예제 ---
        teamDetailResponse1 = new TeamDetailResponse();
        teamDetailResponse1.setTeamId(1L);
        teamDetailResponse1.setChatRoomId(10L);
        teamDetailResponse1.setTeamName("성장하는 A팀");
        teamDetailResponse1.setTeamDomain("growth-team-a");
        teamDetailResponse1.setTeamDescription("함께 성장하며 멋진 포트폴리오를 만들고 싶습니다.");
        teamDetailResponse1.setTeamVive(Set.of(ProjectViveEnum.AGILE));
        teamDetailResponse1.setTeamPreference(Set.of(ProjectGoalEnum.IDEA));
        teamDetailResponse1.setBackendCount(2);
        teamDetailResponse1.setFrontendCount(2);
        teamDetailResponse1.setAiCount(1);
        teamDetailResponse1.setPmCount(1);
        teamDetailResponse1.setDesignCount(0);
        teamDetailResponse1.setLeader(userDetailResponse1);
        teamDetailResponse1.setMembers(List.of(userDetailResponse1, userDetailResponse2));


        // --- 두 번째 팀 상세 정보 예제 ---
        teamDetailResponse2 = new TeamDetailResponse();
        teamDetailResponse2.setTeamId(2L);
        teamDetailResponse2.setChatRoomId(11L);
        teamDetailResponse2.setTeamName("도전하는 B팀");
        teamDetailResponse2.setTeamDomain("challenge-team-b");
        teamDetailResponse2.setTeamDescription("상용화를 목표로 하는 도전적인 팀입니다.");
        teamDetailResponse2.setTeamVive(Set.of(ProjectViveEnum.RULE, ProjectViveEnum.COMFY));
        teamDetailResponse2.setTeamPreference(Set.of(ProjectGoalEnum.IDEA));
        teamDetailResponse2.setBackendCount(3);
        teamDetailResponse2.setFrontendCount(2);
        teamDetailResponse2.setAiCount(1);
        teamDetailResponse2.setPmCount(0);
        teamDetailResponse2.setDesignCount(0);
        teamDetailResponse2.setLeader(userDetailResponse1);
        teamDetailResponse2.setMembers(List.of(userDetailResponse1)); // 다른 멤버 구성

        teamList = List.of(teamDetailResponse1, teamDetailResponse2);
    }
    @Test
    @DisplayName("Team 생성 성공")
    void createTeam_shouldReturn200_whenTeamCreateSuccess() throws Exception {
        when(teamService.createTeam(any(TeamRequest.class)))
                .thenReturn(teamDetailResponse1);
        mockMvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.created().getStatus()))
                .andExpect(jsonPath("$.data.teamId").value(teamDetailResponse1.getTeamId()));
    }

    @Test
    @DisplayName("유효하지 않은 팀 생성 요청")
    void createTeam_shouldReturn400_whenTeamCreateInvalidRequest() throws Exception {
        teamRequest.setTeamDomain(null);
        teamRequest.setTeamName(null);

        mockMvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isBadRequest()) // 400 상태 코드 기대
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REQUEST.getStatus())) // 전역 예외 핸들러 구조에 맞게 검증
                .andExpect(jsonPath("$.message").exists())  // 메시지 필드 존재 여부 확인
                .andExpect(jsonPath("$.message").value(containsString("팀 이름은 필수입니다.")))
                .andExpect(jsonPath("$.message").value(containsString("도메인은 필수입니다.")));
    }

    @Test
    @DisplayName("전체 팀 조회 성공")
    void getAllTeams_shouldReturn200WithTeamList() throws Exception {
        when(teamService.getAllTeams()).thenReturn(teamList);

        // when & then: GET 요청 시 응답 검증
        mockMvc.perform(get("/team") // @GetMapping 경로 확인
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].teamName").value("성장하는 A팀"))
                .andExpect(jsonPath("$.data[1].teamName").value("도전하는 B팀"));
    }

    @Test
    @DisplayName("팀 조건 조회 성공")
    void searchConditionTeam_shouldReturn200WithFilteredTeams() throws Exception {
        when(teamService.searchConditionTeam(any(TeamSearchRequest.class)))
                .thenReturn(teamList);

        teamSearchRequest.setLeaderId(null);    // 리더 ID는 조건 없이 검색

        // when & then
        mockMvc.perform(post("/team/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamSearchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].teamName").value("성장하는 A팀"))
                .andExpect(jsonPath("$.data[1].teamName").value("도전하는 B팀"));
    }

    @Test
    @DisplayName("유효하지 않은 팀 조건 조회")
    void searchConditionTeam_shouldReturn400WithInvalidFilteredTeams() throws Exception {
        when(teamService.searchConditionTeam(any(TeamSearchRequest.class)))
                .thenReturn(teamList);

        teamSearchRequest.setTeamName("1");

        // when & then
        mockMvc.perform(post("/team/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamSearchRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REQUEST.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(containsString("팀 이름은 최소 2자 이상이어야 합니다.")));
    }

    @Test
    @DisplayName("팀 정보 조회 성공")
    void getTeam_shouldReturn200WithTeamDetail() throws Exception {
        when(teamService.getTeam(1L)).thenReturn(teamDetailResponse1);

        // when & then
        mockMvc.perform(get("/team/{teamId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()))
                .andExpect(jsonPath("$.data.teamId").value(1L))
                .andExpect(jsonPath("$.data.teamName").value("성장하는 A팀"))
                .andExpect(jsonPath("$.data.leader.id").value(userDetailResponse1.getId()))
                .andExpect(jsonPath("$.data.members[0].id").value(userDetailResponse1.getId()))
                .andExpect(jsonPath("$.data.members[1].id").value(userDetailResponse2.getId()));
    }

    @Test
    @DisplayName("팀 삭제 성공 - 200 응답")
    void deleteTeam_shouldReturn200_whenDeleteSuccess() throws Exception {
        // 서비스는 반환값이 없으므로 doNothing() 설정 (기본 동작이라 생략 가능)
        doNothing().when(teamService).deleteTeam(teamRequest.getTeamId());

        // when & then
        mockMvc.perform(delete("/team/{teamId}",teamRequest.getTeamId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.noContent().getStatus()))  // ApiResponse.noContent()의 status 값 확인
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("팀 정보 수정 성공")
    void modifyTeam_shouldReturn200WithModifiedTeam() throws Exception {

        when(teamService.modifyTeam(any(TeamRequest.class))).thenReturn(teamDetailResponse2);

        mockMvc.perform(put("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()))
                .andExpect(jsonPath("$.data.teamName").value("도전하는 B팀"));
    }

    @Test
    @DisplayName("팀 정보 수정 - 유효하지 않은 요청일 경우 400 반환")
    void modifyTeam_shouldReturn400_whenInvalidRequest() throws Exception {

        teamRequest.setTeamId(null);   // 필수값 없앰 (예: teamId가 필수일 때)
        teamRequest.setTeamName("");   // 빈 문자열 등

        mockMvc.perform(put("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REQUEST.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(containsString("팀 이름은 필수입니다.")));
    }

    @Test
    @DisplayName("팀 멤버 초대 성공")
    void inviteMemberTeam_shouldReturn200WithTeamDetail() throws Exception {

        when(teamService.inviteMemberTeam(any(TeamInviteRequest.class))).thenReturn(teamDetailResponse1);

        mockMvc.perform(post("/team/invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inviteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()));
    }

    @Test
    @DisplayName("팀 멤버 초대 - 유효하지 않은 요청일 경우 400 반환")
    void inviteMemberTeam_shouldReturn400_whenInvalidRequest() throws Exception {
        inviteRequest.setTeamId(null);  // 필수값 누락

        mockMvc.perform(post("/team/invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inviteRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REQUEST.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(containsString("팀 ID는 필수입니다.")));
    }

    @Test
    @DisplayName("팀 멤버 초대 요청 성공")
    void teamOffer_shouldReturn201WhenCreated() throws Exception {

        doNothing().when(teamMembershipRequestService).requestMemberToTeam(any(TeamOffer.class));

        mockMvc.perform(post("/team/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.created().getStatus()));
    }

    @Test
    @DisplayName("팀 멤버 초대 요청 - 유효하지 않은 요청일 경우 400 반환")
    void teamOffer_shouldReturn400_whenInvalidRequest() throws Exception {
        offer.setRequestType(null);  // 필수값 누락 등

        mockMvc.perform(post("/team/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REQUEST.getStatus()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("팀 멤버 초대 요청 목록 조회 성공 - 단위 테스트")
    void getAllRequest_shouldReturn200AndList() throws Exception {
        // given
        Long teamId = 1L;

        // TeamMembershipResponse DTO 리스트를 만들어서 반환하도록 설정
        List<TeamMembershipResponse> mockResponseList = List.of(
                TeamMembershipResponse.builder()
                        .userId(1L)
                        .userName("홍길동")
                        .requestType(RequestType.INVITE)
                        .status(RequestStatus.PENDING)
                        .message("초대 메시지")
                        .build()
        );

        // service가 teamId로 호출되면 위 리스트 반환하도록 mock 설정
        when(teamMembershipRequestService.getAllRequest(teamId)).thenReturn(mockResponseList);

        // when & then
        mockMvc.perform(get("/team/{teamId}/request", teamId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].userId").value(1L))
                .andExpect(jsonPath("$.data[0].userName").value("홍길동"))
                .andExpect(jsonPath("$.data[0].requestType").value(RequestType.INVITE.toString()))
                .andExpect(jsonPath("$.data[0].status").value(RequestStatus.PENDING.toString()))
                .andExpect(jsonPath("$.data[0].message").value("초대 메시지"));
    }
}

