package com.example.demo.user.dao;

import com.example.demo.user.Enum.PositionEnum;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.Enum.ProjectViveEnum;
import com.example.demo.user.Enum.TechEnum;
import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.chatRoomMembers m " +
            "LEFT JOIN FETCH m.chatRoom " +
            "WHERE u.id = :id")
    Optional<com.example.demo.user.entity.User> findByIdWithChatRoomMembers(@Param("id") Long id);

    @Query(value = """
        SELECT 
            u.team_id,
            u.major,
            GROUP_CONCAT(DISTINCT up.wanted_position ORDER BY up.wanted_position SEPARATOR ',') as positions
        FROM users u
        LEFT JOIN user_wanted_position up ON u.user_id = up.user_user_id
        GROUP BY u.user_id, u.user_name
        """, nativeQuery = true)
    List<Object[]> CountUsers();

    @Query("SELECT u FROM User u WHERE u.team IS NULL")
    List<User> findUsersWithoutTeam();

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN u.wantedPosition wp " +
            "LEFT JOIN u.techStack ts " +
            "LEFT JOIN u.projectVive pv " +
            "LEFT JOIN u.projectGoal pg " +
            "WHERE u.team IS NULL " +
            "AND (:major IS NULL OR u.major = :major) " +
            "AND (:wantedPosition IS NULL OR wp IN :wantedPosition) " +
            "AND (:techStack IS NULL OR ts IN :techStack) " +
            "AND (:projectVive IS NULL OR pv IN :projectVive) " +
            "AND (:projectGoal IS NULL OR pg IN :projectGoal)")
    List<User> findUsersWithoutTeamByFilters(
            Boolean major,
            List<PositionEnum> wantedPosition,
            Set<TechEnum> techStack,
            Set<ProjectViveEnum> projectVive,
            Set<ProjectGoalEnum> projectGoal
    );

    @Query(value = """
        SELECT 
            u.user_id,
            u.user_name,
            GROUP_CONCAT(DISTINCT up.wanted_position ORDER BY up.wanted_position SEPARATOR ',') as positions,
            GROUP_CONCAT(DISTINCT upg.project_preference SEPARATOR ',') as goals,
            GROUP_CONCAT(DISTINCT upv.personal_preference SEPARATOR ',') as vives
        FROM users u
        LEFT JOIN user_wanted_position up ON u.user_id = up.user_user_id
        LEFT JOIN user_project_goal upg ON u.user_id = upg.user_user_id  
        LEFT JOIN user_project_vive upv ON u.user_id = upv.user_user_id
        WHERE u.team_id IS NULL
        GROUP BY u.user_id, u.user_name
        """, nativeQuery = true)
    List<Object[]> findAllCandidates ();

    @Query(value =
            "SELECT DISTINCT u FROM User u " +
                    "LEFT JOIN FETCH u.wantedPosition " +
                    "LEFT JOIN FETCH u.projectGoal " +
                    "LEFT JOIN FETCH u.projectVive " +
                    "WHERE u.id=:id")
    User findCurUser(Long id);
}
