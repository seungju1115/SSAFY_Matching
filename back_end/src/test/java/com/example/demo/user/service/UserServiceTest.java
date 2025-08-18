//package com.example.demo.user.service;
//
//import com.example.demo.user.Enum.PositionEnum;
//import com.example.demo.user.Enum.ProjectGoalEnum;
//import com.example.demo.user.Enum.ProjectViveEnum;
//import com.example.demo.user.Enum.TechEnum;
//import com.example.demo.user.dao.UserRepository;
//import com.example.demo.user.dto.UserSearchRequest;
//import com.example.demo.user.dto.UserSearchResponse;
//import com.example.demo.user.entity.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserService userService;
//
//    private User mockUser1;
//    private User mockUser2;
//
//    @BeforeEach
//    void setUp() {
//        mockUser1 = new User();
//        mockUser1.setId(1L);
//        mockUser1.setUserName("테스트유저1");
//        mockUser1.setUserProfile("백엔드 개발자");
//        mockUser1.setMajor(true);
//        mockUser1.setLastClass(1);
//        mockUser1.setWantedPosition(List.of(PositionEnum.BACKEND));
//        mockUser1.setTechStack(Set.of(TechEnum.SPRING, TechEnum.JPA));
//        mockUser1.setProjectGoal(Set.of(ProjectGoalEnum.STUDY));
//        mockUser1.setProjectVive(Set.of(ProjectViveEnum.values()[0]));
//        mockUser1.setProjectExp("Spring Boot 프로젝트 경험");
//        mockUser1.setQualification("정보처리기사");
//        mockUser1.setTeam(null);
//
//        mockUser2 = new User();
//        mockUser2.setId(2L);
//        mockUser2.setUserName("테스트유저2");
//        mockUser2.setUserProfile("프론트엔드 개발자");
//        mockUser2.setMajor(false);
//        mockUser2.setLastClass(2);
//        mockUser2.setWantedPosition(List.of(PositionEnum.FRONTEND));
//        mockUser2.setTechStack(Set.of(TechEnum.MYSQL));
//        mockUser2.setProjectGoal(Set.of(ProjectGoalEnum.QUICK));
//        mockUser2.setProjectVive(Set.of(ProjectViveEnum.values()[0]));
//        mockUser2.setProjectExp("React 프로젝트 경험");
//        mockUser2.setQualification("웹디자인기능사");
//        mockUser2.setTeam(null);
//    }
//
//    @Test
//    void searchUsersWithoutTeam_정상적인_검색_결과_반환() {
//        // Given
//        UserSearchRequest request = new UserSearchRequest();
//        request.setMajor(true);
//        request.setWantedPosition(List.of(PositionEnum.BACKEND));
//
//        List<User> mockUsers = Arrays.asList(mockUser1);
//        when(userRepository.findUsersWithoutTeamByFilters(
//                eq(true),
//                eq(List.of(PositionEnum.BACKEND)),
//                any(), any(), any(),
//                request.getUserStatus())).thenReturn(mockUsers);
//
//        // When
//        List<UserSearchResponse> result = userService.searchUsersWithoutTeam(request);
//
//        // Then
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
//        assertThat(result.get(0).getUserProfile()).isEqualTo("백엔드 개발자");
//        assertThat(result.get(0).getMajor()).isTrue();
//
//        verify(userRepository).findUsersWithoutTeamByFilters(
//                eq(true),
//                eq(List.of(PositionEnum.BACKEND)),
//                any(), any(), any(),
//                request.getUserStatus());
//    }
//
//    @Test
//    void searchUsersWithoutTeam_검색_결과_없음_빈_리스트_반환() {
//        // Given
//        UserSearchRequest request = new UserSearchRequest();
//        request.setMajor(true);
//        request.setWantedPosition(List.of(PositionEnum.PM));
//
//        when(userRepository.findUsersWithoutTeamByFilters(
//                eq(true),
//                eq(List.of(PositionEnum.PM)),
//                any(), any(), any(),
//                request.getUserStatus())).thenReturn(Collections.emptyList());
//
//        // When
//        List<UserSearchResponse> result = userService.searchUsersWithoutTeam(request);
//
//        // Then
//        assertThat(result).isEmpty();
//        verify(userRepository).findUsersWithoutTeamByFilters(
//                eq(true),
//                eq(List.of(PositionEnum.PM)),
//                any(), any(), any(),
//                request.getUserStatus());
//    }
//
//    @Test
//    void searchUsersWithoutTeam_여러명_검색_결과_반환() {
//        // Given
//        UserSearchRequest request = new UserSearchRequest();
//        request.setMajor(null); // 전공/비전공 구분 없음
//
//        List<User> mockUsers = Arrays.asList(mockUser1, mockUser2);
//        when(userRepository.findUsersWithoutTeamByFilters(
//                any(), any(), any(), any(), any(),
//                request.getUserStatus())).thenReturn(mockUsers);
//
//        // When
//        List<UserSearchResponse> result = userService.searchUsersWithoutTeam(request);
//
//        // Then
//        assertThat(result).hasSize(2);
//        assertThat(result).extracting(UserSearchResponse::getUserName)
//                .containsExactly("테스트유저1", "테스트유저2");
//
//        verify(userRepository).findUsersWithoutTeamByFilters(
//                any(), any(), any(), any(), any(),
//                request.getUserStatus());
//    }
//
//    @Test
//    void searchUsersWithoutTeam_복합_조건_검색() {
//        // Given
//        UserSearchRequest request = new UserSearchRequest();
//        request.setMajor(true);
//        request.setWantedPosition(List.of(PositionEnum.BACKEND));
//        request.setTechStack(Set.of(TechEnum.SPRING));
//        request.setProjectGoal(Set.of(ProjectGoalEnum.STUDY));
//
//        List<User> mockUsers = Arrays.asList(mockUser1);
//        when(userRepository.findUsersWithoutTeamByFilters(
//                eq(true),
//                eq(List.of(PositionEnum.BACKEND)),
//                eq(Set.of(TechEnum.SPRING)),
//                any(),
//                eq(Set.of(ProjectGoalEnum.STUDY)),
//                request.getUserStatus())).thenReturn(mockUsers);
//
//        // When
//        List<UserSearchResponse> result = userService.searchUsersWithoutTeam(request);
//
//        // Then
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
//        assertThat(result.get(0).getTechStack()).contains(TechEnum.SPRING);
//        assertThat(result.get(0).getProjectGoal()).contains(ProjectGoalEnum.STUDY);
//
//        verify(userRepository).findUsersWithoutTeamByFilters(
//                eq(true),
//                eq(List.of(PositionEnum.BACKEND)),
//                eq(Set.of(TechEnum.SPRING)),
//                any(),
//                eq(Set.of(ProjectGoalEnum.STUDY)),
//                request.getUserStatus());
//    }
//
//    @Test
//    void searchUsersWithoutTeam_모든_조건_null_전체_조회() {
//        // Given
//        UserSearchRequest request = new UserSearchRequest();
//        // 모든 필드가 null (기본값)
//
//        List<User> mockUsers = Arrays.asList(mockUser1, mockUser2);
//        when(userRepository.findUsersWithoutTeamByFilters(
//                any(), any(), any(), any(), any(),
//                request.getUserStatus())).thenReturn(mockUsers);
//
//        // When
//        List<UserSearchResponse> result = userService.searchUsersWithoutTeam(request);
//
//        // Then
//        assertThat(result).hasSize(2);
//        verify(userRepository).findUsersWithoutTeamByFilters(
//                any(), any(), any(), any(), any(),
//                request.getUserStatus());
//    }
//
//    @Test
//    void searchUsersWithoutTeam_UserSearchResponse_변환_검증() {
//        // Given
//        UserSearchRequest request = new UserSearchRequest();
//        request.setMajor(true);
//
//        List<User> mockUsers = Arrays.asList(mockUser1);
//        when(userRepository.findUsersWithoutTeamByFilters(
//                any(), any(), any(), any(), any(),
//                request.getUserStatus())).thenReturn(mockUsers);
//
//        // When
//        List<UserSearchResponse> result = userService.searchUsersWithoutTeam(request);
//
//        // Then - 엔티티에서 DTO로의 변환이 올바른지 검증
//        UserSearchResponse response = result.get(0);
//        assertThat(response.getId()).isEqualTo(mockUser1.getId());
//        assertThat(response.getUserName()).isEqualTo(mockUser1.getUserName());
//        assertThat(response.getUserProfile()).isEqualTo(mockUser1.getUserProfile());
//        assertThat(response.getMajor()).isEqualTo(mockUser1.getMajor());
//        assertThat(response.getLastClass()).isEqualTo(mockUser1.getLastClass());
//        assertThat(response.getWantedPosition()).isEqualTo(mockUser1.getWantedPosition());
//        assertThat(response.getTechStack()).isEqualTo(mockUser1.getTechStack());
//        assertThat(response.getProjectGoal()).isEqualTo(mockUser1.getProjectGoal());
//        assertThat(response.getProjectVive()).isEqualTo(mockUser1.getProjectVive());
//        assertThat(response.getProjectExp()).isEqualTo(mockUser1.getProjectExp());
//        assertThat(response.getQualification()).isEqualTo(mockUser1.getQualification());
//    }
//
//}