package com.example.demo.user.dao;

import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.Enum.PersonalPrefEnum;
import com.example.demo.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("local")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User userWithoutTeam1;
    private User userWithoutTeam2;
    private User userWithTeam;

    @BeforeEach
    void setUp() {
        userWithoutTeam1 = new User();
        userWithoutTeam1.setUserName("테스트유저1");
        userWithoutTeam1.setRole("ROLE_USER");
        userWithoutTeam1.setEmail("test1@example.com");
        userWithoutTeam1.setMajor(true);
        userWithoutTeam1.setLastClass(1);
        userWithoutTeam1.setWantedPosition(PositionEnum.BACKEND);
        userWithoutTeam1.setTechStack(Set.of(TechEnum.SPRING, TechEnum.JPA));
        userWithoutTeam1.setProjectPref(Set.of(ProjectGoalEnum.STUDY));
        userWithoutTeam1.setPersonalPref(Set.of(PersonalPrefEnum.values()[0]));
        userWithoutTeam1.setTeam(null);

        userWithoutTeam2 = new User();
        userWithoutTeam2.setUserName("테스트유저2");
        userWithoutTeam2.setRole("ROLE_USER");
        userWithoutTeam2.setEmail("test2@example.com");
        userWithoutTeam2.setMajor(false);
        userWithoutTeam2.setLastClass(2);
        userWithoutTeam2.setWantedPosition(PositionEnum.FRONTEND);
        userWithoutTeam2.setTechStack(Set.of(TechEnum.MYSQL));
        userWithoutTeam2.setProjectPref(Set.of(ProjectGoalEnum.QUICK));
        userWithoutTeam2.setPersonalPref(Set.of(PersonalPrefEnum.values()[0]));
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
    void findUsersWithoutTeamByPosition_포지션으로_필터링() {
        List<User> result = userRepository.findUsersWithoutTeamByPosition(PositionEnum.BACKEND);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("테스트유저1");
        assertThat(result.get(0).getWantedPosition()).isEqualTo(PositionEnum.BACKEND);
    }

    @Test
    void findUsersWithoutTeamByPosition_조건에_맞지_않는_경우_빈_결과() {
        List<User> result = userRepository.findUsersWithoutTeamByPosition(PositionEnum.AI);

        assertThat(result).isEmpty();
    }
}