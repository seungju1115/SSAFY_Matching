package com.example.demo.team.dao;

import com.example.demo.team.entity.TeamLockRequest;
import com.example.demo.team.entity.TeamMembershipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamLockRequestRepository extends JpaRepository<TeamLockRequest, Long> {
    List<TeamLockRequest> findAllByTeamId(Long teamId);
}
