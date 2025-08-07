package com.example.demo.team.controller;

import com.example.demo.auth.filter.JwtFilter;
import com.example.demo.auth.util.JwtUtil;
import com.example.demo.team.dto.*;
import com.example.demo.team.entity.RequestType;
import com.example.demo.team.service.TeamMembershipRequestService;
import com.example.demo.team.service.TeamService;
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

import java.util.List;

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
        teamRequest.setTeamName("테스트 팀");
        teamRequest.setLeaderId(100L);
        teamRequest.setTeamDomain("exampledomain"); // 최소 10자 이상
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

    static private TeamResponse team1;
    static private TeamResponse team2;
    static private TeamDetailResponse detailResponse;
    @BeforeAll
    static void globalSetUp(){
        team1 = new TeamResponse();
        team1.setTeamId(1L);
        team1.setTeamName("테스트A");
        team1.setLeaderId(100L);
        team1.setMemberCount(5);

        team2 = new TeamResponse();
        team2.setTeamId(2L);
        team2.setTeamName("테스트B");
        team2.setLeaderId(200L);
        team2.setMemberCount(3);

        detailResponse = new TeamDetailResponse();
        detailResponse.setTeamId(1L);
        detailResponse.setTeamName("테스트 팀");
        detailResponse.setLeaderId(100L);
        detailResponse.setMembersId(List.of(1L,2L));

    }
    @Test
    @DisplayName("Team 생성 성공")
    void createTeam_shouldReturn200_whenTeamCreateSuccess() throws Exception {
        TeamResponse expectedResponse = new TeamResponse();  // 예상 결과 데이터 생성
        expectedResponse.setTeamId(1L);
        expectedResponse.setTeamName("테스트 팀");
        expectedResponse.setLeaderId(100L);
        expectedResponse.setMemberCount(1);
        when(teamService.createTeam(any(TeamRequest.class)))
                .thenReturn(expectedResponse);
        mockMvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.teamId").value(expectedResponse.getTeamId()));
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
                .andExpect(jsonPath("$.status").value(400)) // 전역 예외 핸들러 구조에 맞게 검증
                .andExpect(jsonPath("$.message").exists())  // 메시지 필드 존재 여부 확인
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("팀 이름은 필수입니다.")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("도메인은 필수입니다.")));
    }

    @Test
    @DisplayName("전체 팀 조회 성공")
    void getAllTeams_shouldReturn200WithTeamList() throws Exception {

        List<TeamResponse> teamList = List.of(team1, team2);

        when(teamService.getAllTeams()).thenReturn(teamList);

        // when & then: GET 요청 시 응답 검증
        mockMvc.perform(get("/team") // @GetMapping 경로 확인
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].teamName").value("테스트A"))
                .andExpect(jsonPath("$.data[1].teamName").value("테스트B"));
    }

    @Test
    @DisplayName("팀 조건 조회 성공")
    void searchConditionTeam_shouldReturn200WithFilteredTeams() throws Exception {

        List<TeamResponse> filteredTeams = List.of(team1, team2);

        when(teamService.searchConditionTeam(any(TeamSearchRequest.class)))
                .thenReturn(filteredTeams);

        teamSearchRequest.setLeaderId(null);    // 리더 ID는 조건 없이 검색

        // when & then
        mockMvc.perform(post("/team/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamSearchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].teamName").value("테스트A"))
                .andExpect(jsonPath("$.data[1].teamName").value("테스트B"));
    }

    @Test
    @DisplayName("유효하지 않은 팀 조건 조회")
    void searchConditionTeam_shouldReturn400WithInvalidFilteredTeams() throws Exception {
        List<TeamResponse> filteredTeams = List.of(team1, team2);

        when(teamService.searchConditionTeam(any(TeamSearchRequest.class)))
                .thenReturn(filteredTeams);

        teamSearchRequest.setTeamName("1");    // 리더 ID는 조건 없이 검색

        // when & then
        mockMvc.perform(post("/team/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamSearchRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("팀 이름은 최소 2자 이상이어야 합니다.")));
    }

    @Test
    @DisplayName("팀 정보 조회 성공")
    void getTeam_shouldReturn200WithTeamDetail() throws Exception {

        when(teamService.getTeam(1L)).thenReturn(detailResponse);

        // when & then
        mockMvc.perform(get("/team/{teamId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.teamId").value(1L))
                .andExpect(jsonPath("$.data.teamName").value("테스트 팀"))
                .andExpect(jsonPath("$.data.leaderId").value(100L))
                .andExpect(jsonPath("$.data.membersId[0]").value(1))
                .andExpect(jsonPath("$.data.membersId[1]").value(2));
    }

    @Test
    @DisplayName("팀 삭제 성공 - 200 응답")
    void deleteTeam_shouldReturn200_whenDeleteSuccess() throws Exception {
        // given
        Long teamId = 1L;
        // 서비스는 반환값이 없으므로 doNothing() 설정 (기본 동작이라 생략 가능)
        doNothing().when(teamService).deleteTeam(teamId);

        // when & then
        mockMvc.perform(delete("/team/{teamId}", teamId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(204))  // ApiResponse.noContent()의 status 값 확인
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("팀 정보 수정 성공")
    void modifyTeam_shouldReturn200WithModifiedTeam() throws Exception {

        when(teamService.modifyTeam(any(TeamRequest.class))).thenReturn(detailResponse);

        mockMvc.perform(put("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.teamName").value("테스트 팀"));
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
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("팀 이름은 필수입니다.")));
    }

    @Test
    @DisplayName("팀 멤버 초대 성공")
    void inviteMemberTeam_shouldReturn200WithTeamDetail() throws Exception {

        when(teamService.inviteMemberTeam(any(TeamInviteRequest.class))).thenReturn(detailResponse);

        mockMvc.perform(post("/team/invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inviteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.teamId").value(1));
    }

    @Test
    @DisplayName("팀 멤버 초대 - 유효하지 않은 요청일 경우 400 반환")
    void inviteMemberTeam_shouldReturn400_whenInvalidRequest() throws Exception {
        inviteRequest.setTeamId(null);  // 필수값 누락

        mockMvc.perform(post("/team/invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inviteRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("팀 ID는 필수입니다.")));
    }

    @Test
    @DisplayName("팀 멤버 초대 요청 성공")
    void teamOffer_shouldReturn201WhenCreated() throws Exception {

        doNothing().when(teamMembershipRequestService).requestMemberToTeam(any(TeamOffer.class));

        mockMvc.perform(post("/team/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(201));
    }

    @Test
    @DisplayName("팀 멤버 초대 요청 - 유효하지 않은 요청일 경우 400 반환")
    void teamOffer_shouldReturn400_whenInvalidRequest() throws Exception {
        offer.setRequestType(null);  // 필수값 누락 등

        mockMvc.perform(post("/team/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("특정 팀의 팀원 조회 성공")
    void getTeamMembers_shouldReturnTeamMemberList() throws Exception {
        Long teamId = 1L;

        List<TeamMemberResponse> members = List.of(
                new TeamMemberResponse(1L, "홍길동"),
                new TeamMemberResponse(2L, "김철수")
        );

        when(teamService.getTeamMembers(teamId)).thenReturn(members);

        mockMvc.perform(get("/team/{teamId}/members", teamId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].memberId").value(1))
                .andExpect(jsonPath("$.data[0].userName").value("홍길동"))
                .andExpect(jsonPath("$.data[1].memberId").value(2))
                .andExpect(jsonPath("$.data[1].userName").value("김철수"));
    }

}

