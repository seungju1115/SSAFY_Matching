package com.example.demo.team.dao;

import com.example.demo.team.entity.TeamMembershipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMembershipRequestRepository extends JpaRepository<TeamMembershipRequest, Long> {
        List<TeamMembershipRequest> findAllByTeamId(Long teamId);
}
