package com.example.demo.dashboard;

import com.example.demo.dashboard.dto.DashboardResponseDto;
import com.example.demo.dashboard.service.DashboardService;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DashboardService 단위 테스트")
class DashboardServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private List<Object[]> mockDomainCounts;

    @BeforeEach
    void setUp() {
        mockDomainCounts = createMockDomainCounts();
    }

    @Test
    @DisplayName("getDashboard 메서드 - 정상적인 데이터 반환")
    void getDashboard_ShouldReturnCorrectDashboardData() {
        // Given
        List<Object[]> testUsers = createBasicTestUsers();

        when(userRepository.CountUsers()).thenReturn(testUsers);
        when(teamRepository.countDomain()).thenReturn(mockDomainCounts);

        // When
        DashboardResponseDto result = dashboardService.getDashboard();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getWhole()).isEqualTo(5);
        assertThat(result.getMatchedMajor()).isEqualTo(2);  // user1, user2
        assertThat(result.getMathcedUnmajor()).isEqualTo(1); // user3
        assertThat(result.getUnmatchedMajor()).isEqualTo(1); // user4
        assertThat(result.getUnmatchedUnmajor()).isEqualTo(1); // user5

        // Position별 매칭 상태 검증
        assertThat(result.getBack_main()).isEqualTo(3);
        assertThat(result.getBack_sub()).isEqualTo(0); // user4
        assertThat(result.getFront_main()).isEqualTo(1); // user2
        assertThat(result.getFront_sub()).isEqualTo(1); // user5
        assertThat(result.getAi_main()).isEqualTo(1); // user2
        assertThat(result.getAi_sub()).isEqualTo(0);

        // Domain 카운트 검증
        assertThat(result.getDomain()).hasSize(3);
        assertThat(result.getDomain().get("웹개발")).isEqualTo(5L);
        assertThat(result.getDomain().get("AI")).isEqualTo(3L);
        assertThat(result.getDomain().get("모바일")).isEqualTo(2L);

        // Repository 호출 검증
        verify(userRepository, times(1)).CountUsers();
        verify(teamRepository, times(1)).countDomain();
    }

    @Test
    @DisplayName("빈 사용자 목록에 대한 처리")
    void getDashboard_WithEmptyUserList_ShouldReturnZeroValues() {
        // Given
        when(userRepository.CountUsers()).thenReturn(Collections.emptyList());
        when(teamRepository.countDomain()).thenReturn(Collections.emptyList());

        // When
        DashboardResponseDto result = dashboardService.getDashboard();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getWhole()).isEqualTo(0);
        assertThat(result.getMatchedMajor()).isEqualTo(0);
        assertThat(result.getMathcedUnmajor()).isEqualTo(0);
        assertThat(result.getUnmatchedMajor()).isEqualTo(0);
        assertThat(result.getUnmatchedUnmajor()).isEqualTo(0);
        assertThat(result.getBack_main()).isEqualTo(0);
        assertThat(result.getBack_sub()).isEqualTo(0);
        assertThat(result.getDomain()).isEmpty();
    }

    @Test
    @DisplayName("모든 사용자가 팀에 매칭된 경우")
    void getDashboard_AllUsersMatched_ShouldReturnCorrectCounts() {
        // Given
        List<Object[]> allMatchedUsers = createAllMatchedUsers();

        when(userRepository.CountUsers()).thenReturn(allMatchedUsers);
        when(teamRepository.countDomain()).thenReturn(mockDomainCounts);

        // When
        DashboardResponseDto result = dashboardService.getDashboard();

        // Then
        assertThat(result.getWhole()).isEqualTo(3);
        assertThat(result.getMatchedMajor()).isEqualTo(2);
        assertThat(result.getMathcedUnmajor()).isEqualTo(1);
        assertThat(result.getUnmatchedMajor()).isEqualTo(0);
        assertThat(result.getUnmatchedUnmajor()).isEqualTo(0);
        assertThat(result.getBack_main()).isEqualTo(1);
        assertThat(result.getFront_main()).isEqualTo(1);
        assertThat(result.getAi_main()).isEqualTo(1);
    }

    @Test
    @DisplayName("모든 사용자가 팀에 매칭되지 않은 경우")
    void getDashboard_NoUsersMatched_ShouldReturnCorrectCounts() {
        // Given
        List<Object[]> noMatchedUsers = createNoMatchedUsers();

        when(userRepository.CountUsers()).thenReturn(noMatchedUsers);
        when(teamRepository.countDomain()).thenReturn(Collections.emptyList());

        // When
        DashboardResponseDto result = dashboardService.getDashboard();

        // Then
        assertThat(result.getWhole()).isEqualTo(2);
        assertThat(result.getMatchedMajor()).isEqualTo(0);
        assertThat(result.getMathcedUnmajor()).isEqualTo(0);
        assertThat(result.getUnmatchedMajor()).isEqualTo(1);
        assertThat(result.getUnmatchedUnmajor()).isEqualTo(1);
        assertThat(result.getBack_sub()).isEqualTo(0);
        assertThat(result.getFront_sub()).isEqualTo(0);
        assertThat(result.getBack_main()).isEqualTo(1);
        assertThat(result.getFront_main()).isEqualTo(1);
    }

    @Test
    @DisplayName("팀 매칭 상태에 따른 정확한 분류")
    void getDashboard_ShouldDistinguishBetweenTeamAndNoTeam() {
        // Given
        List<Object[]> mixedUsers = createMixedTeamUsers();

        when(userRepository.CountUsers()).thenReturn(mixedUsers);
        when(teamRepository.countDomain()).thenReturn(mockDomainCounts);

        // When
        DashboardResponseDto result = dashboardService.getDashboard();

        // Then - 디버깅 정보 출력

        System.out.println("=== 테스트 결과 ===");
        System.out.println("전체 사용자: " + result.getWhole());
        System.out.println("전공자 팀 있음: " + result.getMatchedMajor());
        System.out.println("전공자 팀 없음: " + result.getUnmatchedMajor());
        System.out.println("비전공자 팀 있음: " + result.getMathcedUnmajor());
        System.out.println("비전공자 팀 없음: " + result.getUnmatchedUnmajor());

        assertThat(result.getWhole()).as("전체 사용자 수").isEqualTo(4);
        assertThat(result.getMatchedMajor()).as("전공자 중 팀 있음").isEqualTo(1);
        assertThat(result.getUnmatchedMajor()).as("전공자 중 팀 없음").isEqualTo(1);
        assertThat(result.getMathcedUnmajor()).as("비전공자 중 팀 있음").isEqualTo(1);
        assertThat(result.getUnmatchedUnmajor()).as("비전공자 중 팀 없음").isEqualTo(1);
    }

    @Test
    @DisplayName("Repository 예외 발생 시 처리")
    void getDashboard_WhenDomainCountFails_ShouldThrowException() {
        // Given
        List<Object[]> testUsers = createBasicTestUsers();

        when(userRepository.CountUsers()).thenReturn(testUsers);
        when(teamRepository.countDomain()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            dashboardService.getDashboard();
        });
    }

    // === Helper Methods ===

    private List<Object[]> createBasicTestUsers() {
        List<Object[]> users = new ArrayList<>();

        // 1. 전공자, 팀 있음, BACKEND
        // [team_id, major, positions]
        users.add(new Object[]{1L, true, "BACKEND"});

        // 2. 전공자, 팀 있음, FRONTEND + AI
        users.add(new Object[]{2L, true, "AI,FRONTEND"});

        // 3. 비전공자, 팀 있음, BACKEND
        users.add(new Object[]{3L, false, "BACKEND"});

        // 4. 전공자, 팀 없음, BACKEND
        users.add(new Object[]{null, true, "BACKEND"});

        // 5. 비전공자, 팀 없음, FRONTEND
        users.add(new Object[]{null, false, "FRONTEND"});

        return users;
    }

    private List<Object[]> createAllMatchedUsers() {
        List<Object[]> users = new ArrayList<>();

        // 전공자, 팀 있음, BACKEND
        users.add(new Object[]{1L, true, "BACKEND"});

        // 전공자, 팀 있음, FRONTEND
        users.add(new Object[]{2L, true, "FRONTEND"});

        // 비전공자, 팀 있음, AI
        users.add(new Object[]{3L, false, "AI"});

        return users;
    }

    private List<Object[]> createNoMatchedUsers() {
        List<Object[]> users = new ArrayList<>();

        // 전공자, 팀 없음, BACKEND
        users.add(new Object[]{null, true, "BACKEND"});

        // 비전공자, 팀 없음, FRONTEND
        users.add(new Object[]{null, false, "FRONTEND"});

        return users;
    }

    private List<Object[]> createMultiPositionUsers() {
        List<Object[]> users = new ArrayList<>();

        // 전공자, 팀 있음, 여러 포지션 (BACKEND, FRONTEND, AI)
        // GROUP_CONCAT은 알파벳 순서로 정렬됨
        users.add(new Object[]{1L, true, "AI,BACKEND,FRONTEND"});

        return users;
    }

    private List<Object[]> createMixedTeamUsers() {
        List<Object[]> users = new ArrayList<>();

        // 전공자, 팀 있음, BACKEND
        users.add(new Object[]{1L, true, "BACKEND"});

        // 전공자, 팀 없음, FRONTEND
        users.add(new Object[]{null, true, "FRONTEND"});

        // 비전공자, 팀 있음, AI
        users.add(new Object[]{2L, false, "AI"});

        // 비전공자, 팀 없음, DESIGN
        users.add(new Object[]{null, false, "DESIGN"});

        return users;
    }

    private Team createTeam(Long id, String teamName, String domain) {
        Team team = new Team();
        team.setId(id);
        team.setTeamName(teamName);
        team.setTeamDomain(domain);
        return team;
    }

    private List<Object[]> createMockDomainCounts() {
        List<Object[]> domainCounts = new ArrayList<>();
        domainCounts.add(new Object[]{"웹개발", 5L});
        domainCounts.add(new Object[]{"AI", 3L});
        domainCounts.add(new Object[]{"모바일", 2L});
        return domainCounts;
    }
}