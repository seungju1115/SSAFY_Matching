package com.example.demo.user.controller;

import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectPrefEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.dto.SearchUserRequest;
import com.example.demo.user.dto.SearchUserResponse;
import com.example.demo.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("local")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

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
        mockResponse1.setTechStack(Set.of(TechEnum.Spring, TechEnum.JPA));
        mockResponse1.setProjectPref(Set.of(ProjectPrefEnum.STABLE));
        mockResponse1.setProjectExp("Spring Boot 프로젝트 경험");
        mockResponse1.setQualification("정보처리기사");

        mockResponse2 = new SearchUserResponse();
        mockResponse2.setId(2L);
        mockResponse2.setUserName("테스트유저2");
        mockResponse2.setUserProfile("프론트엔드 개발자");
        mockResponse2.setMajor(false);
        mockResponse2.setLastClass(2);
        mockResponse2.setWantedPosition(PositionEnum.FRONTEND);
        mockResponse2.setTechStack(Set.of(TechEnum.MySQL));
        mockResponse2.setProjectPref(Set.of(ProjectPrefEnum.CHALLENGE));
        mockResponse2.setProjectExp("React 프로젝트 경험");
        mockResponse2.setQualification("웹디자인기능사");
    }

    @Test
    @WithMockUser
    void searchUsersWithoutTeam_성공_모든_사용자_반환() throws Exception {
        SearchUserRequest request = new SearchUserRequest();
        List<SearchUserResponse> mockResponses = Arrays.asList(mockResponse1, mockResponse2);

        when(userService.searchUsersWithoutTeam(any(SearchUserRequest.class)))
                .thenReturn(mockResponses);

        mockMvc.perform(post("/users/profile/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].userName").value("테스트유저1"))
                .andExpect(jsonPath("$.data[0].wantedPosition").value("BACKEND"))
                .andExpect(jsonPath("$.data[1].userName").value("테스트유저2"))
                .andExpect(jsonPath("$.data[1].wantedPosition").value("FRONTEND"));
    }

    @Test
    @WithMockUser
    void searchUsersWithoutTeam_포지션_필터링() throws Exception {
        SearchUserRequest request = new SearchUserRequest();
        request.setWantedPosition(PositionEnum.BACKEND);
        
        List<SearchUserResponse> mockResponses = Arrays.asList(mockResponse1);

        when(userService.searchUsersWithoutTeam(any(SearchUserRequest.class)))
                .thenReturn(mockResponses);

        mockMvc.perform(post("/users/profile/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].userName").value("테스트유저1"))
                .andExpect(jsonPath("$.data[0].wantedPosition").value("BACKEND"));
    }

    @Test
    @WithMockUser
    void searchUsersWithoutTeam_기술스택_필터링() throws Exception {
        SearchUserRequest request = new SearchUserRequest();
        request.setTechStack(Set.of(TechEnum.Spring));
        
        List<SearchUserResponse> mockResponses = Arrays.asList(mockResponse1);

        when(userService.searchUsersWithoutTeam(any(SearchUserRequest.class)))
                .thenReturn(mockResponses);

        mockMvc.perform(post("/users/profile/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].userName").value("테스트유저1"))
                .andExpect(jsonPath("$.data[0].techStack").isArray());
    }

    @Test
    @WithMockUser
    void searchUsersWithoutTeam_프로젝트선호도_필터링() throws Exception {
        SearchUserRequest request = new SearchUserRequest();
        request.setProjectPref(Set.of(ProjectPrefEnum.STABLE));
        
        List<SearchUserResponse> mockResponses = Arrays.asList(mockResponse1);

        when(userService.searchUsersWithoutTeam(any(SearchUserRequest.class)))
                .thenReturn(mockResponses);

        mockMvc.perform(post("/users/profile/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].userName").value("테스트유저1"))
                .andExpect(jsonPath("$.data[0].projectPref").isArray());
    }

    @Test
    @WithMockUser
    void searchUsersWithoutTeam_복합_조건_필터링() throws Exception {
        SearchUserRequest request = new SearchUserRequest();
        request.setWantedPosition(PositionEnum.BACKEND);
        request.setTechStack(Set.of(TechEnum.Spring, TechEnum.JPA));
        request.setProjectPref(Set.of(ProjectPrefEnum.STABLE));
        
        List<SearchUserResponse> mockResponses = Arrays.asList(mockResponse1);

        when(userService.searchUsersWithoutTeam(any(SearchUserRequest.class)))
                .thenReturn(mockResponses);

        mockMvc.perform(post("/users/profile/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].userName").value("테스트유저1"))
                .andExpect(jsonPath("$.data[0].wantedPosition").value("BACKEND"))
                .andExpect(jsonPath("$.data[0].techStack").isArray())
                .andExpect(jsonPath("$.data[0].projectPref").isArray());
    }

    @Test
    @WithMockUser
    void searchUsersWithoutTeam_조건에_맞는_사용자_없음() throws Exception {
        SearchUserRequest request = new SearchUserRequest();
        request.setWantedPosition(PositionEnum.MISC);
        
        when(userService.searchUsersWithoutTeam(any(SearchUserRequest.class)))
                .thenReturn(Arrays.asList());

        mockMvc.perform(post("/users/profile/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    @WithMockUser
    void searchUsersWithoutTeam_잘못된_요청_형식() throws Exception {
        String invalidJson = "{ \"invalidField\": \"value\" }";

        mockMvc.perform(post("/users/profile/search")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isOk());
    }
}