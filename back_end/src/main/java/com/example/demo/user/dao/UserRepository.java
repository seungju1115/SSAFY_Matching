package com.example.demo.user.dao;

import com.example.demo.ai.dto.CandidateDto;
import com.example.demo.dashboard.dto.UserCountDto;
import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.chatRoomMembers m " +
            "LEFT JOIN FETCH m.chatRoom " +
            "WHERE u.id = :id")
    Optional<com.example.demo.user.entity.User> findByIdWithChatRoomMembers(@Param("id") Long id);

    @Query(value = "select u from User u")
    List<User> CountUsers();

    @Query("SELECT u FROM User u WHERE u.team IS NULL")
    List<User> findUsersWithoutTeam();
    
    @Query("SELECT u FROM User u WHERE u.team IS NULL AND :wantedPosition MEMBER OF u.wantedPosition")
    List<User> findUsersWithoutTeamByPosition(@Param("wantedPosition") PositionEnum wantedPosition);

    @Query(value =
            "SELECT DISTINCT u FROM User u " +
                    "LEFT JOIN FETCH u.wantedPosition " +
                    "LEFT JOIN FETCH u.projectGoal " +
                    "LEFT JOIN FETCH u.ProjectVive " +
                    "WHERE u.team IS NULL OR u.team.id != :teamId")
    List<User> findAllCandidates(long teamId);

    @Query(value =
            "SELECT DISTINCT u FROM User u " +
                    "LEFT JOIN FETCH u.wantedPosition " +
                    "LEFT JOIN FETCH u.projectGoal " +
                    "LEFT JOIN FETCH u.ProjectVive " +
                    "WHERE u.id=:id")
    User findCurUser(Long id);
}
