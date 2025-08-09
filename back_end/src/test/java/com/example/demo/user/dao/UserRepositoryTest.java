package com.example.demo.user.dao;

import com.example.demo.user.Enum.*;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User userWithoutTeam1;
    private User userWithoutTeam2;

    @BeforeEach
    void setUp() {
        userWithoutTeam1 = new User();
        userWithoutTeam1.setUserName("테스트유저1");
        userWithoutTeam1.setRole("student");
        userWithoutTeam1.setEmail("test1@example.com");
        userWithoutTeam1.setMajor(true);
        userWithoutTeam1.setLastClass(1);
        userWithoutTeam1.setWantedPosition(List.of(PositionEnum.BACKEND));
        userWithoutTeam1.setTechStack(Set.of(TechEnum.SPRING, TechEnum.JPA));
        userWithoutTeam1.setProjectGoal(Set.of(ProjectGoalEnum.STUDY));
        userWithoutTeam1.setProjectVive(Set.of(ProjectViveEnum.values()[0]));
        userWithoutTeam1.setTeam(null);

        userWithoutTeam2 = new User();
        userWithoutTeam2.setUserName("테스트유저2");
        userWithoutTeam2.setRole("student");
        userWithoutTeam2.setEmail("test2@example.com");
        userWithoutTeam2.setMajor(false);
        userWithoutTeam2.setLastClass(2);
        userWithoutTeam2.setWantedPosition(List.of(PositionEnum.FRONTEND));
        userWithoutTeam2.setTechStack(Set.of(TechEnum.MYSQL));
        userWithoutTeam2.setProjectGoal(Set.of(ProjectGoalEnum.QUICK));
        userWithoutTeam2.setProjectVive(Set.of(ProjectViveEnum.values()[0]));
        userWithoutTeam2.setTeam(null);

        entityManager.persistAndFlush(userWithoutTeam1);
        entityManager.persistAndFlush(userWithoutTeam2);
    }

    @Test
    void findUsersWithoutTeam_모든_팀없는_유저_반환() {
        List<User> result = userRepository.findUsersWithoutTeam();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(User::getUserName)
                .containsExactlyInAnyOrder("테스트유저1", "테스트유저2");
    }

    @Test
    void findUsersWithoutTeamByFilters_검색된_사용자가_없는_경우_빈_리스트_반환() {
        // Given: 존재하지 않는 조건으로 검색
        
        // When
        List<User> result = userRepository.findUsersWithoutTeamByFilters(
                null,  // major
                List.of(PositionEnum.PM),  // 존재하지 않는 포지션
                null,  // techStack
                null,  // projectVive
                null   // projectGoal
        );

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findUsersWithoutTeamByFilters_major_전공자_필터링() {
        // When: 전공자만 검색
        List<User> result = userRepository.findUsersWithoutTeamByFilters(
                true,  // major = 전공자
                null, null, null, null
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(result.get(0).getMajor()).isTrue();
    }

    @Test
    void findUsersWithoutTeamByFilters_major_비전공자_필터링() {
        // When: 비전공자만 검색
        List<User> result = userRepository.findUsersWithoutTeamByFilters(
                false,  // major = 비전공자
                null, null, null, null
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저2");
        assertThat(result.get(0).getMajor()).isFalse();
    }

    @Test
    void findUsersWithoutTeamByFilters_wantedPosition_필터링() {
        // When: BACKEND 포지션으로 검색
        List<User> result = userRepository.findUsersWithoutTeamByFilters(
                null,
                List.of(PositionEnum.BACKEND),  // wantedPosition
                null, null, null
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(result.get(0).getWantedPosition()).contains(PositionEnum.BACKEND);
    }

    @Test
    void findUsersWithoutTeamByFilters_techStack_필터링() {
        // When: SPRING 기술스택으로 검색
        List<User> result = userRepository.findUsersWithoutTeamByFilters(
                null, null,
                Set.of(TechEnum.SPRING),  // techStack
                null, null
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(result.get(0).getTechStack()).contains(TechEnum.SPRING);
    }

    @Test
    void findUsersWithoutTeamByFilters_projectGoal_필터링() {
        // When: STUDY 프로젝트 목표로 검색
        List<User> result = userRepository.findUsersWithoutTeamByFilters(
                null, null, null, null,
                Set.of(ProjectGoalEnum.STUDY)  // projectGoal
        );

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(result.get(0).getProjectGoal()).contains(ProjectGoalEnum.STUDY);
    }

    @Test
    void findUsersWithoutTeamByFilters_복합_조건_필터링() {
        // When: 전공자 + BACKEND 포지션 + SPRING 기술스택으로 검색
        List<User> result = userRepository.findUsersWithoutTeamByFilters(
                true,  // major
                List.of(PositionEnum.BACKEND),  // wantedPosition
                Set.of(TechEnum.SPRING),  // techStack
                null, null
        );

        // Then: 모든 조건을 만족하는 테스트유저1만 반환
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
    }

    @Test
    void findUsersWithoutTeamByFilters_복합_조건_매칭되지_않음() {
        // When: 비전공자 + BACKEND 포지션 (매칭되지 않는 조건)
        List<User> result = userRepository.findUsersWithoutTeamByFilters(
                false,  // major = 비전공자
                List.of(PositionEnum.BACKEND),  // wantedPosition = BACKEND
                null, null, null
        );

        // Then: 조건에 맞는 사용자가 없음 (테스트유저2는 비전공자지만 FRONTEND)
        assertThat(result).isEmpty();
    }

    @Test
    void findUsersWithoutTeamByFilters_모든_조건_null이면_전체_반환() {
        // When: 모든 필터 조건이 null
        List<User> result = userRepository.findUsersWithoutTeamByFilters(
                null, null, null, null, null
        );

        // Then: 팀이 없는 모든 유저 반환
        assertThat(result).hasSize(2);
        assertThat(result).extracting(User::getUserName)
                .containsExactlyInAnyOrder("테스트유저1", "테스트유저2");
    }

}