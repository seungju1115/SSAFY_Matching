package com.example.demo.user.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.dto.SearchUserRequest;
import com.example.demo.user.dto.SearchUserResponse;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private SearchUserResponse mockResponse1;
    private SearchUserResponse mockResponse2;

    @BeforeEach
    void setUp() {
        mockResponse1 = new SearchUserResponse();
        mockResponse1.setId(1L);
        mockResponse1.setUserName("테스트유저1");
        mockResponse1.setUserProfile("백엔드 개발자");
        mockResponse1.setMajor(true);
        mockResponse1.setLastClass(1);
        mockResponse1.setWantedPosition(PositionEnum.BACKEND);
        mockResponse1.setTechStack(Set.of(TechEnum.SPRING, TechEnum.JPA));
        mockResponse1.setProjectGoal(Set.of(ProjectGoalEnum.STUDY));
        mockResponse1.setProjectExp("Spring Boot 프로젝트 경험");
        mockResponse1.setQualification("정보처리기사");

        mockResponse2 = new SearchUserResponse();
        mockResponse2.setId(2L);
        mockResponse2.setUserName("테스트유저2");
        mockResponse2.setUserProfile("프론트엔드 개발자");
        mockResponse2.setMajor(false);
        mockResponse2.setLastClass(2);
        mockResponse2.setWantedPosition(PositionEnum.FRONTEND);
        mockResponse2.setTechStack(Set.of(TechEnum.MYSQL));
        mockResponse2.setProjectGoal(Set.of(ProjectGoalEnum.QUICK));
        mockResponse2.setProjectExp("React 프로젝트 경험");
        mockResponse2.setQualification("웹디자인기능사");
    }

    @Test
    void searchUsersWithoutTeam_성공_모든_사용자_반환() {
        SearchUserRequest request = new SearchUserRequest();
        List<SearchUserResponse> mockResponses = Arrays.asList(mockResponse1, mockResponse2);

        when(userService.searchUsersWithoutTeam(any(SearchUserRequest.class)))
                .thenReturn(mockResponses);

        ResponseEntity<ApiResponse> response = userController.searchUsersWithoutTeam(request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getStatus()).isEqualTo(200);
        
        @SuppressWarnings("unchecked")
        List<SearchUserResponse> data = (List<SearchUserResponse>) response.getBody().getData();
        assertThat(data).hasSize(2);
        assertThat(data.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(data.get(0).getWantedPosition()).isEqualTo(PositionEnum.BACKEND);
        assertThat(data.get(1).getUserName()).isEqualTo("테스트유저2");
        assertThat(data.get(1).getWantedPosition()).isEqualTo(PositionEnum.FRONTEND);
    }

    @Test
    void searchUsersWithoutTeam_포지션_필터링() {
        SearchUserRequest request = new SearchUserRequest();
        request.setWantedPosition(PositionEnum.BACKEND);
        
        List<SearchUserResponse> mockResponses = Arrays.asList(mockResponse1);

        when(userService.searchUsersWithoutTeam(any(SearchUserRequest.class)))
                .thenReturn(mockResponses);

        ResponseEntity<ApiResponse> response = userController.searchUsersWithoutTeam(request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getStatus()).isEqualTo(200);
        
        @SuppressWarnings("unchecked")
        List<SearchUserResponse> data = (List<SearchUserResponse>) response.getBody().getData();
        assertThat(data).hasSize(1);
        assertThat(data.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(data.get(0).getWantedPosition()).isEqualTo(PositionEnum.BACKEND);
    }

    @Test
    void searchUsersWithoutTeam_기술스택_필터링() {
        SearchUserRequest request = new SearchUserRequest();
        request.setTechStack(Set.of(TechEnum.SPRING));
        
        List<SearchUserResponse> mockResponses = Arrays.asList(mockResponse1);

        when(userService.searchUsersWithoutTeam(any(SearchUserRequest.class)))
                .thenReturn(mockResponses);

        ResponseEntity<ApiResponse> response = userController.searchUsersWithoutTeam(request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getStatus()).isEqualTo(200);
        
        @SuppressWarnings("unchecked")
        List<SearchUserResponse> data = (List<SearchUserResponse>) response.getBody().getData();
        assertThat(data).hasSize(1);
        assertThat(data.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(data.get(0).getTechStack()).contains(TechEnum.SPRING);
    }

    @Test
    void searchUsersWithoutTeam_조건에_맞는_사용자_없음() {
        SearchUserRequest request = new SearchUserRequest();
        request.setWantedPosition(PositionEnum.AI);
        
        when(userService.searchUsersWithoutTeam(any(SearchUserRequest.class)))
                .thenReturn(Arrays.asList());

        ResponseEntity<ApiResponse> response = userController.searchUsersWithoutTeam(request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getStatus()).isEqualTo(200);
        
        @SuppressWarnings("unchecked")
        List<SearchUserResponse> data = (List<SearchUserResponse>) response.getBody().getData();
        assertThat(data).isEmpty();
    }
}