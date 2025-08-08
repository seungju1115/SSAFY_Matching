package com.example.demo.chat;

import com.example.demo.DemoApplication;
import com.example.demo.auth.filter.JwtFilter;
import com.example.demo.auth.util.JwtUtil;
import com.example.demo.chat.dao.ChatRoomRepository;
import com.example.demo.chat.dto.ChatMessageRequest;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.entity.RoomType;
import com.example.demo.chat.service.ChatMessageService;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.team.dto.TeamDetailResponse;
import com.example.demo.team.dto.TeamRequest;
import com.example.demo.team.service.TeamService;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class ChatIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private JwtFilter jwtFilter; // JwtFilter 자체를 mock

    @MockitoBean
    private JwtUtil jwtUtil; // JwtUtil도 mock해버리면 해결됨

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private TeamService teamService;

    private User user1,user2,user3;
    private TeamDetailResponse teamDetailResponse;
    private ChatMessageRequest chatMessageRequest;
    private ChatRoomRequest chatRoomRequest;
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

        TeamRequest teamRequest = new TeamRequest();
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

        teamDetailResponse = teamService.createTeam(teamRequest);
        teamDetailResponse = teamService.getTeam(teamDetailResponse.getTeamId());

        chatMessageRequest = new ChatMessageRequest();
        chatMessageRequest.setRoomId(teamDetailResponse.getChatRoomId());
        chatMessageRequest.setSenderId(user1.getId());
        chatMessageRequest.setMessage("Hello World");

        chatMessageService.saveMessage(chatMessageRequest);

        chatRoomRequest = new ChatRoomRequest();
        chatRoomRequest.setRoomType(RoomType.PRIVATE);
        chatRoomRequest.setUser1Id(user2.getId());
        chatRoomRequest.setUser2Id(user3.getId());
    }

    @Test
    @DisplayName("채팅방 메시지 조회 성공 통합 테스트")
    void getChatMessages_success() throws Exception {
        mockMvc.perform(get("/chatroom/{chatRoomId}/messages", teamDetailResponse.getChatRoomId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.ok().getStatus()))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].chatRoomId").value(teamDetailResponse.getChatRoomId()))
                .andExpect(jsonPath("$.data[0].message").value("Hello World"));
    }

    @Test
    @DisplayName("1:1 채팅방 생성 성공 통합 테스트")
    void createPrivateChatRoom_success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(chatRoomRequest);

        mockMvc.perform(post("/chatroom/private")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.created().getStatus()))
                .andExpect(jsonPath("$.data.roomId").exists())
                .andExpect(jsonPath("$.data.roomType").value(RoomType.PRIVATE.name()));
    }

    @Test
    @DisplayName("1:1 채팅방 생성 실패 통합 테스트 - 유저 없음")
    void createPrivateChatRoom_failWithNotFoundUser() throws Exception {
        chatRoomRequest.setUser1Id(999L);
        String jsonRequest = objectMapper.writeValueAsString(chatRoomRequest);

        mockMvc.perform(post("/chatroom/private")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(ErrorCode.USER_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("1:1 채팅방 생성 실패 통합 테스트 - 유효하지 않은 요청")
    void createPrivateChatRoom_failWithInvalidRequest() throws Exception {
        chatRoomRequest.setRoomType(null);
        String jsonRequest = objectMapper.writeValueAsString(chatRoomRequest);

        mockMvc.perform(post("/chatroom/private")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REQUEST.getStatus()))
                .andExpect(jsonPath("$.message").value(containsString("roomType")));
    }


}
