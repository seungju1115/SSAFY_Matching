package com.example.demo.ai;

import com.example.demo.ai.dto.CandidateDto;
import com.example.demo.ai.dto.PersonToTeamDto;
import com.example.demo.ai.dto.TeamAIDto;
import com.example.demo.ai.dto.TeamToPersonDto;
import com.example.demo.ai.service.AIService;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("AIService 단위 테스트")
class AIServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AIService aiService;

    private final String RECSYS_BASE_URL = "http://test-recsys:8000";

    @BeforeEach
    void setUp() {
        // @Value로 주입되는 값을 테스트용으로 설정
        ReflectionTestUtils.setField(aiService, "recsysBaseUrl", RECSYS_BASE_URL);
    }

    @Test
    @DisplayName("팀 ID로 TeamToPersonDto 조회 성공")
    void findTeamToPersonDtoById_Success() {
        // Given
        Long teamId = 1L;
        Team mockTeam = createMockTeam();
        Object[] mockUserData = createMockUserObjectArray();

        when(teamRepository.findTeamAIDtoById(teamId)).thenReturn(mockTeam);

        // 올바른 방법: List<Object[]> 생성
        List<Object[]> candidateList = Collections.singletonList(mockUserData);
        doReturn(candidateList).when(userRepository).findAllCandidates();

        // When
        TeamToPersonDto result = aiService.findTeamToPersonDtoById(teamId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCurrentTeam()).isNotNull();
        assertThat(result.getCandidates()).hasSize(1);

        verify(teamRepository).findTeamAIDtoById(teamId);
        verify(userRepository).findAllCandidates();
    }

    @Test
    @DisplayName("개인 ID로 PersonToTeamDto 조회 성공")
    void findPersonToTeamDtoById_Success() {
        // Given
        Long personId = 1L;
        User mockUser = createMockUser();
        List<Team> mockTeams = Arrays.asList(createMockTeam());

        when(userRepository.findCurUser(personId)).thenReturn(mockUser);
        when(teamRepository.findAvailableTeams()).thenReturn(mockTeams);

        // When
        PersonToTeamDto result = aiService.findPersonToTeamDtoById(personId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPerson()).isNotNull();
        assertThat(result.getTeams()).hasSize(1);

        verify(userRepository).findCurUser(personId);
        verify(teamRepository).findAvailableTeams();
    }

    @Test
    @DisplayName("팀에게 후보자 추천 - RecSys 호출 성공")
    void recommendCandidatesForTeam_Success() {
        // Given
        Long teamId = 1L;
        Team mockTeam = createMockTeam();
        Object[] mockUserData = createMockUserObjectArray();

        when(teamRepository.findTeamAIDtoById(teamId)).thenReturn(mockTeam);

        // 올바른 방법: List<Object[]> 생성
        List<Object[]> candidateList = Collections.singletonList(mockUserData);
        doReturn(candidateList).when(userRepository).findAllCandidates();

        // RecSys 응답 모킹
        Map<String, Object> recsysResponse = createMockRecsysResponse();
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(recsysResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(responseEntity);

        // When
        List<CandidateDto> result = aiService.recommendCandidatesForTeam(teamId, false);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        verify(restTemplate).postForEntity(
                eq(RECSYS_BASE_URL + "/recommend/candidates"),
                any(),
                eq(Map.class)
        );
    }

    @Test
    @DisplayName("개인에게 팀 추천 - RecSys 호출 성공")
    void recommendTeamsForPerson_Success() {
        // Given
        Long personId = 1L;
        User mockUser = createMockUser();
        List<Team> mockTeams = Arrays.asList(createMockTeam());

        when(userRepository.findCurUser(personId)).thenReturn(mockUser);
        when(teamRepository.findAvailableTeams()).thenReturn(mockTeams);

        // RecSys 응답 모킹
        Map<String, Object> recsysResponse = createMockTeamRecsysResponse();
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(recsysResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(responseEntity);

        // When
        List<TeamAIDto> result = aiService.recommendTeamsForPerson(personId, false);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        verify(restTemplate).postForEntity(
                eq(RECSYS_BASE_URL + "/recommend/teams"),
                any(),
                eq(Map.class)
        );
    }

    @Test
    @DisplayName("후보자 추천 - RecSys 호출 실패시 폴백 로직 동작")
    void recommendCandidatesForTeam_FallbackWhenRecsysFails() {
        // Given
        Long teamId = 1L;
        Team mockTeam = createMockTeam();
        Object[] mockUserData = createMockUserObjectArray();

        when(teamRepository.findTeamAIDtoById(teamId)).thenReturn(mockTeam);

        // 올바른 방법: List<Object[]> 생성
        List<Object[]> candidateList = Collections.singletonList(mockUserData);
        doReturn(candidateList).when(userRepository).findAllCandidates();

        // RecSys 호출 실패 시뮬레이션
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenThrow(new RuntimeException("RecSys connection failed"));

        // When
        List<CandidateDto> result = aiService.recommendCandidatesForTeam(teamId, false);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1); // 폴백 로직으로 기본 데이터 반환

        verify(restTemplate).postForEntity(anyString(), any(), eq(Map.class));
    }

    @Test
    @DisplayName("팀 추천 - RecSys 호출 실패시 폴백 로직 동작")
    void recommendTeamsForPerson_FallbackWhenRecsysFails() {
        // Given
        Long personId = 1L;
        User mockUser = createMockUser();
        List<Team> mockTeams = Arrays.asList(createMockTeam());

        when(userRepository.findCurUser(personId)).thenReturn(mockUser);
        when(teamRepository.findAvailableTeams()).thenReturn(mockTeams);

        // RecSys 호출 실패 시뮬레이션
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenThrow(new RuntimeException("RecSys connection failed"));

        // When
        List<TeamAIDto> result = aiService.recommendTeamsForPerson(personId, false);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(0); // 폴백 로직으로 기본 데이터 반환

        verify(restTemplate).postForEntity(anyString(), any(), eq(Map.class));
    }

    // ==================== 테스트 헬퍼 메서드들 ====================

    private Team createMockTeam() {
        Team team = mock(Team.class);
        when(team.getId()).thenReturn(1L);
        when(team.getTeamName()).thenReturn("Test Team");
        // memberWanted 필드도 모킹 (폴백 로직에서 사용)

        when(team.getMemberWanted()).thenReturn("BACKEND", "FRONTEND");
        return team;
    }

    private User createMockUser() {
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getUserName()).thenReturn("testuser");
        when(user.getWantedPosition()).thenReturn(List.of(PositionEnum.BACKEND, PositionEnum.FRONTEND));

        // Set을 직접 생성하여 반환
        Set<ProjectGoalEnum> goals = new HashSet<>();
        goals.add(ProjectGoalEnum.STUDY);
        when(user.getProjectGoal()).thenReturn(goals);

        Set<ProjectViveEnum> vives = new HashSet<>();
        vives.add(ProjectViveEnum.CASUAL);
        when(user.getProjectVive()).thenReturn(vives);

        return user;
    }

    private Object[] createMockUserObjectArray() {
        // userRepository.findAllCandidates()가 반환하는 Object[] 형태
        return new Object[]{
                1L, // userId
                "testuser", // userName
                "BACKEND,FRONTEND", // positions
                "STUDY", // goals
                "CASUAL" // vives
        };
    }

    private Map<String, Object> createMockRecsysResponse() {
        Map<String, Object> result = new HashMap<>();
        result.put("user_id", "1");
        result.put("similarity", 0.85);

        Map<String, Object> response = new HashMap<>();
        response.put("results", Arrays.asList(result));
        response.put("status", "success");

        return response;
    }

    private Map<String, Object> createMockTeamRecsysResponse() {
        Map<String, Object> result = new HashMap<>();
        result.put("team_id", "1");
        result.put("similarity", 0.90);

        Map<String, Object> response = new HashMap<>();
        response.put("results", Arrays.asList(result));
        response.put("status", "success");

        return response;
    }
}