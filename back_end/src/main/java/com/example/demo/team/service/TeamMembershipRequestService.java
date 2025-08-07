package com.example.demo.team.service;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.team.dao.TeamMembershipRequestRepository;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.dto.*;
import com.example.demo.team.entity.RequestStatus;
import com.example.demo.team.entity.RequestType;
import com.example.demo.team.entity.Team;
import com.example.demo.team.entity.TeamMembershipRequest;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMembershipRequestService {

    private final TeamMembershipRequestRepository teamMembershipRequestRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamService teamService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void requestTeamToMember(TeamOffer teamOffer){
        Team team = teamRepository.findById(teamOffer.getTeamId()).orElseThrow(()-> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
        User user = userRepository.findById(teamOffer.getUserId()).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        boolean exists = team.getMembershipRequests().stream()
                .anyMatch(req ->
                        req.getUser().equals(user)
                                && req.getRequestType() == RequestType.INVITE
                                && req.getStatus() != RequestStatus.REJECTED
                );

        if (exists) throw new BusinessException(ErrorCode.TEAM_REQUEST_ALLREADY_EXIST);

        saveTeamOffer(teamOffer, team, user);
        messagingTemplate.convertAndSend("/queue/team/offer/" + teamOffer.getUserId(), teamOffer.getMessage());
    }

    @Transactional
    public void requestMemberToTeam(TeamOffer teamOffer) {
        Team team = teamRepository.findById(teamOffer.getTeamId()).orElseThrow(()-> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
        User user = userRepository.findById(teamOffer.getUserId()).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        boolean exists = user.getMembershipRequests().stream()
                .anyMatch(req ->
                        req.getTeam().equals(team)
                                && req.getRequestType() == RequestType.JOIN_REQUEST
                                && req.getStatus() != RequestStatus.REJECTED
                );

        if (exists) throw new BusinessException(ErrorCode.TEAM_REQUEST_ALLREADY_EXIST);

        saveTeamOffer(teamOffer, team, user);

        for (TeamMemberResponse teamMemberResponse : teamService.getTeamMembers(teamOffer.getTeamId())) {
            messagingTemplate.convertAndSend("/queue/team/offer/" + teamMemberResponse.getMemberId(), teamOffer.getMessage());
        }
    }

    private void saveTeamOffer(TeamOffer teamOffer, Team team, User user) {

        TeamMembershipRequest request = new TeamMembershipRequest();
        request.setTeam(team);
        request.setUser(user);
        request.setRequestType(teamOffer.getRequestType());  // 팀에서 사용자에게 초대
        request.setStatus(RequestStatus.PENDING);
        request.setMessage(teamOffer.getMessage()); // 메시지가 있다면

        teamMembershipRequestRepository.save(request);
    }
}
