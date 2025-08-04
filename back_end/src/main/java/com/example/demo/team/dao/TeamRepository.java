package com.example.demo.team.dao;

import com.example.demo.team.entity.Team;
import jakarta.persistence.MapKeyColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamName(String name);


    @Query(value = "select t.teamDomain, count(*) from Team t group by t.teamDomain")
    @MapKeyColumn(name = "teamDomain")
    List<Object[]> countDomain();
}
