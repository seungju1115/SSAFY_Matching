package com.example.demo.user.service;

import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.dto.SearchUserRequest;
import com.example.demo.user.dto.SearchUserResponse;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser1;
    private User mockUser2;

    @BeforeEach
    void setUp() {
        mockUser1 = new User();
        mockUser1.setId(1L);
        mockUser1.setUserName("테스트유저1");
        mockUser1.setUserProfile("백엔드 개발자");
        mockUser1.setMajor(true);
        mockUser1.setLastClass(1);
        mockUser1.setWantedPosition(PositionEnum.BACKEND);
        mockUser1.setTechStack(Set.of(TechEnum.SPRING, TechEnum.JPA));
        mockUser1.setProjectPref(Set.of(ProjectGoalEnum.STUDY));
        mockUser1.setPersonalPref(Set.of(PersonalPrefEnum.values()[0]));
        mockUser1.setProjectExp("Spring Boot 프로젝트 경험");
        mockUser1.setQualification("정보처리기사");
        mockUser1.setTeam(null);

        mockUser2 = new User();
        mockUser2.setId(2L);
        mockUser2.setUserName("테스트유저2");
        mockUser2.setUserProfile("프론트엔드 개발자");
        mockUser2.setMajor(false);
        mockUser2.setLastClass(2);
        mockUser2.setWantedPosition(PositionEnum.FRONTEND);
        mockUser2.setTechStack(Set.of(TechEnum.MYSQL));
        mockUser2.setProjectPref(Set.of(ProjectGoalEnum.QUICK));
        mockUser2.setPersonalPref(Set.of(PersonalPrefEnum.values()[0]));
        mockUser2.setProjectExp("React 프로젝트 경험");
        mockUser2.setQualification("웹디자인기능사");
        mockUser2.setTeam(null);
    }

    @Test
    void searchUsersWithoutTeam_성공_여러_사용자_반환() {
        SearchUserRequest request = new SearchUserRequest();
        request.setWantedPosition(null);
        request.setTechStack(null);
        request.setProjectPref(null);

        List<User> mockUsers = Arrays.asList(mockUser1, mockUser2);
        
        when(userRepository.findUsersWithoutTeam())
                .thenReturn(mockUsers);

        List<SearchUserResponse> result = userService.searchUsersWithoutTeam(request);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(result.get(0).getWantedPosition()).isEqualTo(PositionEnum.BACKEND);
        assertThat(result.get(1).getUserName()).isEqualTo("테스트유저2");
        assertThat(result.get(1).getWantedPosition()).isEqualTo(PositionEnum.FRONTEND);

        verify(userRepository).findUsersWithoutTeam();
    }

    @Test
    void searchUsersWithoutTeam_포지션_필터링() {
        SearchUserRequest request = new SearchUserRequest();
        request.setWantedPosition(PositionEnum.BACKEND);
        request.setTechStack(null);
        request.setProjectPref(null);

        List<User> mockUsers = Arrays.asList(mockUser1);
        
        when(userRepository.findUsersWithoutTeamByPosition(eq(PositionEnum.BACKEND)))
                .thenReturn(mockUsers);

        List<SearchUserResponse> result = userService.searchUsersWithoutTeam(request);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(result.get(0).getWantedPosition()).isEqualTo(PositionEnum.BACKEND);

        verify(userRepository).findUsersWithoutTeamByPosition(eq(PositionEnum.BACKEND));
    }

    @Test
    void searchUsersWithoutTeam_기술스택_필터링() {
        SearchUserRequest request = new SearchUserRequest();
        request.setWantedPosition(null);
        request.setTechStack(Set.of(TechEnum.SPRING));
        request.setProjectPref(null);

        List<User> mockUsers = Arrays.asList(mockUser1);
        
        when(userRepository.findUsersWithoutTeam())
                .thenReturn(Arrays.asList(mockUser1, mockUser2));

        List<SearchUserResponse> result = userService.searchUsersWithoutTeam(request);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(result.get(0).getTechStack()).contains(TechEnum.SPRING);

        verify(userRepository).findUsersWithoutTeam();
    }

    @Test
    void searchUsersWithoutTeam_프로젝트선호도_필터링() {
        SearchUserRequest request = new SearchUserRequest();
        request.setWantedPosition(null);
        request.setTechStack(null);
        request.setProjectPref(Set.of(ProjectGoalEnum.STUDY));

        List<User> mockUsers = Arrays.asList(mockUser1);
        
        when(userRepository.findUsersWithoutTeam())
                .thenReturn(Arrays.asList(mockUser1, mockUser2));

        List<SearchUserResponse> result = userService.searchUsersWithoutTeam(request);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(result.get(0).getProjectPref()).contains(ProjectGoalEnum.STUDY);

        verify(userRepository).findUsersWithoutTeam();
    }

    @Test
    void searchUsersWithoutTeam_모든_조건_복합_필터링() {
        SearchUserRequest request = new SearchUserRequest();
        request.setWantedPosition(PositionEnum.BACKEND);
        request.setTechStack(Set.of(TechEnum.SPRING, TechEnum.JPA));
        request.setProjectPref(Set.of(ProjectGoalEnum.STUDY));

        List<User> mockUsers = Arrays.asList(mockUser1);
        
        when(userRepository.findUsersWithoutTeamByPosition(eq(PositionEnum.BACKEND)))
                .thenReturn(mockUsers);

        List<SearchUserResponse> result = userService.searchUsersWithoutTeam(request);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(result.get(0).getWantedPosition()).isEqualTo(PositionEnum.BACKEND);
        assertThat(result.get(0).getTechStack()).containsAll(Set.of(TechEnum.SPRING, TechEnum.JPA));
        assertThat(result.get(0).getProjectPref()).contains(ProjectGoalEnum.STUDY);

        verify(userRepository).findUsersWithoutTeamByPosition(eq(PositionEnum.BACKEND));
    }

    @Test
    void searchUsersWithoutTeam_조건에_맞는_사용자_없음() {
        SearchUserRequest request = new SearchUserRequest();
        request.setWantedPosition(PositionEnum.AI);
        request.setTechStack(Set.of(TechEnum.DOCKER));
        request.setProjectPref(Set.of(ProjectGoalEnum.QUICK));

        when(userRepository.findUsersWithoutTeamByPosition(eq(PositionEnum.AI)))
                .thenReturn(Arrays.asList());

        List<SearchUserResponse> result = userService.searchUsersWithoutTeam(request);

        assertThat(result).isEmpty();

        verify(userRepository).findUsersWithoutTeamByPosition(eq(PositionEnum.AI));
    }
}