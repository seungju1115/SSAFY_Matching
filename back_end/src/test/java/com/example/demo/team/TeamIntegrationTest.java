package com.example.demo.team;

import com.example.demo.DemoApplication;
import com.example.demo.auth.filter.JwtFilter;
import com.example.demo.auth.util.JwtUtil;
import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.RoomType;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.dto.TeamInviteRequest;
import com.example.demo.team.dto.TeamOffer;
import com.example.demo.team.dto.TeamRequest;
import com.example.demo.team.dto.TeamSearchRequest;
import com.example.demo.team.entity.RequestType;
import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class TeamIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @MockitoBean
    private JwtFilter jwtFilter; // JwtFilter 자체를 mock

    @MockitoBean
    private JwtUtil jwtUtil; // JwtUtil도 mock해버리면 해결됨

    private TeamRequest teamRequest;
    private User user1,user2,user3;
    private Team team1;
    private TeamSearchRequest teamSearchRequest;
    private TeamInviteRequest teamInviteRequest;
    private TeamOffer teamOffer;

    @BeforeEach
    void setUp() {
        // User 1
        user1 = new User();
        user1.setUserName("홍길동");
        user1.setRole("USER");
        user1.setEmail("hong@example.com");
        user1.setMajor(true);
        user1.setLastClass(1);
        user1.setWantedPosition(new ArrayList<>(Arrays.asList(PositionEnum.DESIGN)));
        user1.setTechStack(new HashSet<>(Arrays.asList(TechEnum.JPA, TechEnum.JPA)));
        user1.setProjectGoal(new HashSet<>(Arrays.asList(ProjectGoalEnum.AWARD)));
        user1.setProjectVive(new HashSet<>(Arrays.asList(ProjectViveEnum.AGILE)));

        // User 2
        user2 = new User();
        user2.setUserName("김철수");
        user2.setRole("USER");
        user2.setEmail("kim@example.com");
        user2.setMajor(false);
        user2.setLastClass(2);
        user2.setWantedPosition(new ArrayList<>(Arrays.asList(PositionEnum.DESIGN)));
        user2.setTechStack(new HashSet<>(Arrays.asList(TechEnum.JPA, TechEnum.JPA)));
        user2.setProjectGoal(new HashSet<>(Arrays.asList(ProjectGoalEnum.AWARD)));
        user2.setProjectVive(new HashSet<>(Arrays.asList(ProjectViveEnum.AGILE)));

        // User 3
        user3 = new User();
        user3.setUserName("이영희");
        user3.setRole("USER");
        user3.setEmail("lee@example.com");
        user3.setMajor(true);
        user3.setLastClass(3);
        user3.setWantedPosition(new ArrayList<>(Arrays.asList(PositionEnum.DESIGN)));
        user3.setTechStack(new HashSet<>(Arrays.asList(TechEnum.JPA, TechEnum.JPA)));
        user3.setProjectGoal(new HashSet<>(Arrays.asList(ProjectGoalEnum.AWARD)));
        user3.setProjectVive(new HashSet<>(Arrays.asList(ProjectViveEnum.AGILE)));

        userRepository.saveAll(List.of(user1, user2, user3));

        team1 = new Team();
        team1.setTeamName("테스트 팀");
        team1.setTeamDomain("testteamdomain123"); // 최소 길이 10 이상이어야 함
        team1.setTeamDescription("통합 테스트용 팀 설명입니다.");
        team1.setTeamPreference(new HashSet<>(Arrays.asList(ProjectGoalEnum.AWARD, ProjectGoalEnum.IDEA))); // ✅ 가변 Set
        team1.setLeader(user2);
        team1.setMembershipRequests(new ArrayList<>());
        // 1. 팀 먼저 저장
        teamRepository.save(team1); // team1에 ID 발급됨

// 2. chatRoom1에 저장된 team1 세팅 후 저장
        ChatRoom chatRoom1 = new ChatRoom();
        chatRoom1.setTeam(team1);
        chatRoom1.setRoomType(RoomType.TEAM);
        chatRoomRepository.save(chatRoom1);

// 3. user2 팀 설정 후 저장
        user2.setTeam(team1);
        userRepository.save(user2);

        teamRequest = new TeamRequest();
        teamRequest.setTeamName("test team");
        teamRequest.setTeamDomain("testDomainTestTime");
        teamRequest.setLeaderId(user1.getId());
        teamRequest.setTeamId(1L);
        teamRequest.setTeamVive(new HashSet<>(Arrays.asList(ProjectViveEnum.AGILE)));
        teamRequest.setTeamPreference(new HashSet<>(Arrays.asList(ProjectGoalEnum.IDEA)));
        teamRequest.setBackendCount(2);
        teamRequest.setFrontendCount(2);
        teamRequest.setAiCount(1);
        teamRequest.setPmCount(1);
        teamRequest.setDesignCount(0);
        teamRequest.setTeamDescription("함께 성장하며 멋진 포트폴리오를 만들고 싶습니다.");

        teamSearchRequest = new TeamSearchRequest();
        teamSearchRequest.setTeamName("테스트 팀"); // 검색 조건 예시

        teamInviteRequest = new TeamInviteRequest();
        teamInviteRequest.setTeamId(team1.getId());   // 초대할 팀
        teamInviteRequest.setUserId(user3.getId());   // 초대할 사용자

        teamOffer = new TeamOffer();
        teamOffer.setTeamId(team1.getId());
        teamOffer.setUserId(user1.getId());
        teamOffer.setMessage("함께 합시다.");
        teamOffer.setRequestType(RequestType.INVITE); // 초대 요청
    }

    @Test
    @DisplayName("팀 생성 - 통합 테스트")
    void createTeam_success() throws Exception {
        mockMvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.created().getStatus())) // ApiResponse.created()에 맞는 상태코드 확인
                .andExpect(jsonPath("$.data.teamName").value(teamRequest.getTeamName()));
    }

    @Test
    @DisplayName("팀 생성 - 통합 테스트 유저 정보 찾을 수 없음")
    void createTeam_return404WithUserNotFound() throws Exception {
        teamRequest.setLeaderId(999L);
        mockMvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isNotFound()) // 예외 처리 시 반환 상태 코드
                .andExpect(jsonPath("$.status").value(ErrorCode.USER_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("팀 생성 - 통합 테스트 유저 팀 보유 중")
    void createTeam_return400WithUserAllreadyHasTeam() throws Exception {
        // 1. user1로 팀 한 번 생성
        teamRequest.setLeaderId(user1.getId());
        mockMvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isOk());

        // 2. 같은 user1로 다시 팀 생성 시도 → 이미 팀 있음 예외
        mockMvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.USER_ALLREADY_HAS_TEAM.getStatus()))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_ALLREADY_HAS_TEAM.getMessage()));
    }

    @Test
    @DisplayName("팀 생성 - 통합 테스트 유효하지 않은 요청")
    void createTeam_return400WithInvalidRequest() throws Exception {
        teamRequest.setLeaderId(null);

        mockMvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REQUEST.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(containsString("leaderId")));;
    }

    @Test
    @DisplayName("팀 전체 조회 - 통합 테스트")
    void getAllTeams()throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/team")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("팀 조건 검색 - 통합 테스트 성공")
    void searchConditionTeam_success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(teamSearchRequest);

        // when & then
        mockMvc.perform(post("/team/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].teamName").value(team1.getTeamName()));
    }

    @Test
    @DisplayName("팀 조건 검색 - 통합 테스트 실패 유효하지 않은 조건의 요청")
    void searchConditionTeam_FailWithInvalidRequest() throws Exception {
        teamSearchRequest.setTeamName("굿");
        String jsonRequest = objectMapper.writeValueAsString(teamSearchRequest);

        // when & then
        mockMvc.perform(post("/team/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REQUEST.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(containsString("teamName")));
    }

    @Test
    @DisplayName("팀 정보 검색 - 통합 테스트 성공")
    void getTeam_success() throws Exception {
        mockMvc.perform(get("/team/{teamId}", team1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()))
                .andExpect(jsonPath("$.data.teamName").value(team1.getTeamName()));
    }

    @Test
    @DisplayName("팀 정보 검색 - 통합 테스트 없는 팀 아이디")
    void getTeam_failWithNotFoundTeam() throws Exception {
        mockMvc.perform(get("/team/{teamId}", 99999L) // 존재하지 않는 ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(ErrorCode.TEAM_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(ErrorCode.TEAM_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("팀 삭제 - 통합 테스트 성공")
    void deleteTeam_success() throws Exception {
        mockMvc.perform(delete("/team/{teamId}", team1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.noContent().getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(ApiResponse.noContent().getMessage()));
    }

    @Test
    @DisplayName("팀 삭제 - 통합 테스트 없는 팀 아이디")
    void deleteTeam_failWithNotFoundTeam() throws Exception {
        mockMvc.perform(delete("/team/{teamId}", 99999L) // 존재하지 않는 ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(ErrorCode.TEAM_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(ErrorCode.TEAM_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("팀 수정 - 통합 테스트 성공")
    void modifyTeam_success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(teamRequest);

        mockMvc.perform(put("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()))
                .andExpect(jsonPath("$.data.teamName").value(teamRequest.getTeamName()));
    }

    @Test
    @DisplayName("팀 수정 - 통합 테스트 없는 팀 아이디")
    void modifyTeam_failWithNotFoundTeam() throws Exception {
        teamRequest.setTeamId(9999L);
        String jsonRequest = objectMapper.writeValueAsString(teamRequest);

        mockMvc.perform(put("/team") // 존재하지 않는 ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(ErrorCode.TEAM_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(ErrorCode.TEAM_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("팀 수정 - 통합 테스트 없는 유저 아이디")
    void modifyTeam_failWithNotFoundUser() throws Exception {
        teamRequest.setLeaderId(9999L);
        String jsonRequest = objectMapper.writeValueAsString(teamRequest);

        mockMvc.perform(put("/team") // 존재하지 않는 ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(ErrorCode.USER_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("팀 수정 - 통합 테스트 팀명 중복")
    void modifyTeam_failWithAllreadyHasTeamName() throws Exception {
        teamRequest.setTeamName("테스트 팀");
        String jsonRequest = objectMapper.writeValueAsString(teamRequest);

        mockMvc.perform(put("/team") // 존재하지 않는 ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.TEAM_NAME_ALREADY_EXISTS.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(ErrorCode.TEAM_NAME_ALREADY_EXISTS.getMessage()));
    }

    @Test
    @DisplayName("팀 수정 - 통합 테스트 유효하지 않은 요청")
    void modifyTeam_failWithInvalidRequest() throws Exception {
        teamRequest.setLeaderId(null);
        String jsonRequest = objectMapper.writeValueAsString(teamRequest);

        mockMvc.perform(put("/team") // 존재하지 않는 ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REQUEST.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(containsString("leaderId")));
    }

    @Test
    @DisplayName("팀 멤버 초대 - 통합 테스트 성공")
    void inviteMemberTeam_success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(teamInviteRequest);

        // when & then
        mockMvc.perform(post("/team/invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()));
    }

    @Test
    @DisplayName("팀 멤버 초대 - 통합 테스트 실패 유효하지 않은 요청")
    void inviteMemberTeam_failWithInvalidRequest() throws Exception {
        teamInviteRequest.setTeamId(null);
        String jsonRequest = objectMapper.writeValueAsString(teamInviteRequest);

        // when & then
        mockMvc.perform(post("/team/invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REQUEST.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(containsString("teamId")));
    }

    @Test
    @DisplayName("팀 멤버 초대 - 통합 테스트 실패 팀 없음")
    void inviteMemberTeam_failWithNotFoundTeam() throws Exception {
        teamInviteRequest.setTeamId(9999L);
        String jsonRequest = objectMapper.writeValueAsString(teamInviteRequest);

        // when & then
        mockMvc.perform(post("/team/invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(ErrorCode.TEAM_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(ErrorCode.TEAM_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("팀 멤버 초대 - 통합 테스트 실패 유저 id 없음")
    void inviteMemberTeam_failWithUserNotFound() throws Exception {
        teamInviteRequest.setUserId(9999L);
        String jsonRequest = objectMapper.writeValueAsString(teamInviteRequest);

        // when & then
        mockMvc.perform(post("/team/invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(ErrorCode.USER_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("팀 멤버 초대 - 통합 테스트 실패 이미 존재하는 유저")
    void inviteMemberTeam_failWithAllreadyHasMember() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(teamInviteRequest);

        mockMvc.perform(post("/team/invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
        // when & then
        mockMvc.perform(post("/team/invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.USER_ALLREADY_HAS_TEAM.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_ALLREADY_HAS_TEAM.getMessage()));
    }

    @Test
    @DisplayName("팀 멤버 초대 - 통합 테스트 실패 유효하지 않은 채팅방 타입")
    void inviteMemberTeam_failWithInvalidChatRoomType() throws Exception {
        team1.getChatRoom().setRoomType(RoomType.PRIVATE);
        String jsonRequest = objectMapper.writeValueAsString(teamInviteRequest);

        // when & then
        mockMvc.perform(post("/team/invitation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_CHAT_ROOM_TYPE.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_CHAT_ROOM_TYPE.getMessage()));
    }

    @Test
    @DisplayName("팀 멤버 초대 요청 - 통합 테스트 성공")
    void teamOffer_inviteRequest_success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(teamOffer);

        mockMvc.perform(post("/team/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.created().getStatus()))
                .andExpect(jsonPath("$.data").doesNotExist());  // Void 타입이므로 null 또는 없음 기대
    }

    @Test
    @DisplayName("팀 멤버 초대 - 통합 테스트 실패 유효하지 않은 요청")
    void teamOffer_failWithInvalidRequest() throws Exception {
        teamOffer.setTeamId(null);
        String jsonRequest = objectMapper.writeValueAsString(teamOffer);

        mockMvc.perform(post("/team/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REQUEST.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(containsString("teamId")));
    }


    @Test
    @DisplayName("팀 멤버 초대 요청 - 통합 테스트 실패 유저 id 없음")
    void teamOffer_failWithUserNotFound() throws Exception {
        teamOffer.setUserId(9999L);
        String jsonRequest = objectMapper.writeValueAsString(teamOffer);

        mockMvc.perform(post("/team/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(ErrorCode.USER_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("팀 멤버 초대 요청 - 통합 테스트 실패 이미 존재하는 요청")
    void teamOffer_failWithAllreadyHasOffer() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(teamOffer);

        mockMvc.perform(post("/team/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
        // when & then
        mockMvc.perform(post("/team/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.TEAM_REQUEST_ALLREADY_EXIST.getStatus()))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value(ErrorCode.TEAM_REQUEST_ALLREADY_EXIST.getMessage()));
    }

    @Test
    @DisplayName("팀 멤버 초대 요청 목록 조회 - 통합 테스트 성공")
    void getAllRequest_success() throws Exception {
        // given
        Long teamId = team1.getId();  // @BeforeEach에서 생성된 팀 ID 사용

        // when & then
        mockMvc.perform(get("/team/{teamId}/request", teamId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()))
                .andExpect(jsonPath("$.data").isArray());
                // data 배열 안에 최소 하나의 요청 객체가 있고, userName 필드가 존재하는지 확인
    }
}

