package com.example.demo.team.dao;

import com.example.demo.team.entity.Team;
import com.example.demo.team.entity.TeamMembershipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMembershipRequestRepository extends JpaRepository<TeamMembershipRequest, Long> {

}
