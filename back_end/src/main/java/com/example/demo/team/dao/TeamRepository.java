package com.example.demo.team.dao;

import com.example.demo.ai.dto.CandidateDto;
import com.example.demo.ai.dto.TeamAIDto;
import com.example.demo.team.entity.Team;
import jakarta.persistence.MapKeyColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamName(String name);

    @Query("SELECT DISTINCT t FROM Team t JOIN FETCH t.leader LEFT JOIN FETCH t.members")
    List<Team> findAllWithDetails();

    @Query(value = "select t.teamDomain, count(*) from Team t group by t.teamDomain")
    @MapKeyColumn(name = "teamDomain")
    List<Object[]> countDomain();

    @Query(value = "SELECT DISTINCT t FROM Team t " +
            "LEFT JOIN FETCH t.teamPreference " +
            "LEFT JOIN FETCH t.teamVive " +
            "LEFT JOIN FETCH t.members " +
            "WHERE SIZE(t.members) < 6")
    List<Team> findAvailableTeams();

    @Query(value = "SELECT DISTINCT t FROM Team t " +
            "LEFT JOIN FETCH t.teamPreference " +
            "LEFT JOIN FETCH t.teamVive " +
            "LEFT JOIN FETCH t.members " +
            "WHERE t.id=:id")
    Team findTeamAIDtoById(Long id);
}
