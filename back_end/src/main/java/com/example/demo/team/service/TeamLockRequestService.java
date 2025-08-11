package com.example.demo.team.service;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.team.dao.TeamLockRequestRepository;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.dto.TeamLockCreateRequest;
import com.example.demo.team.dto.TeamLockResponse;
import com.example.demo.team.entity.RequestStatus;
import com.example.demo.team.entity.Team;
import com.example.demo.team.entity.TeamLockRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamLockRequestService {
    private final TeamLockRequestRepository teamLockRequestRepository;
    private final TeamRepository teamRepository;

    public List<TeamLockResponse> getAllLockRequest(){
        return teamLockRequestRepository.findAll().stream()
                .map(TeamLockResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveRequest(TeamLockCreateRequest teamLockCreateRequest){
        List<TeamLockRequest> requests = teamLockRequestRepository.findAllByTeamId(teamLockCreateRequest.getTeamId());

        boolean hasPending = requests.stream()
                .anyMatch(request -> request.getStatus() == RequestStatus.PENDING);

        if (hasPending) {
            throw new BusinessException(ErrorCode.LOCKREQUEST_ALLREADY_HAS);
        }

        Team team = teamRepository.findById(teamLockCreateRequest.getTeamId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));

        TeamLockRequest teamLockRequest = new TeamLockRequest();
        teamLockRequest.setTeam(team);
        teamLockRequest.setMessage(teamLockRequest.getMessage());
        teamLockRequestRepository.save(teamLockRequest);
    }

//    public TeamLockResponse getLockRequest(Long teamId){
//        return TeamLockResponse.fromEntity(teamLockRequestRepository.findAllByTeamId(teamId));
//    }
}
