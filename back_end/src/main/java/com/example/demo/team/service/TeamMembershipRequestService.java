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
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.cp.lock.exception.LockAcquireLimitReachedException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMembershipRequestService {

    public final TeamMembershipRequestRepository teamMembershipRequestRepository;
    public final TeamRepository teamRepository;
    public final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final HazelcastInstance hazelcastInstance;

    @Transactional
    public void requestTeamToMember(TeamOffer teamOffer) {
        Team team = teamRepository.findById(teamOffer.getTeamId()).orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
        User user = userRepository.findById(teamOffer.getUserId()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String key = team.getId() + "+" + user.getId();
        FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(key);

        if (!lock.tryLock()) {
            throw new LockAcquireLimitReachedException("요청이 처리 중입니다. 잠시 후 다시 시도해주세요.");
        }

        try {
            boolean exists = team.getMembershipRequests().stream()
                    .anyMatch(req -> req.getUser().equals(user) && req.getStatus() != RequestStatus.REJECTED);

            if (exists) {
                throw new BusinessException(ErrorCode.TEAM_REQUEST_ALLREADY_EXIST);
            }

            saveTeamOffer(teamOffer, team, user);

        } finally {
            lock.unlock();
        }

        messagingTemplate.convertAndSend("/queue/team/offer/" + teamOffer.getUserId(), teamOffer.getMessage());
    }
    @Transactional
    public void requestMemberToTeam(TeamOffer teamOffer) {
        Team team = teamRepository.findById(teamOffer.getTeamId()).orElseThrow(()-> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
        User user = userRepository.findById(teamOffer.getUserId()).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String key=team.getId() + "+" + user.getId();
        FencedLock lock=hazelcastInstance.getCPSubsystem().getLock(key);

        if (!lock.tryLock()) {
            throw new LockAcquireLimitReachedException("요청이 처리 중입니다. 잠시 후 다시 시도해주세요.");
        }

        try {
            boolean exists = team.getMembershipRequests().stream()
                    .anyMatch(req -> req.getTeam().equals(team) && req.getStatus() != RequestStatus.REJECTED);

            if (exists) {
                throw new BusinessException(ErrorCode.TEAM_REQUEST_ALLREADY_EXIST);
            }

            saveTeamOffer(teamOffer, team, user);

        } finally {
            lock.unlock();
        }

        for (User member : team.getMembers()) {
            messagingTemplate.convertAndSend("/queue/team/offer/" + member.getId(), teamOffer.getMessage());
        }
    }

    @Transactional
    public void saveTeamOffer(TeamOffer teamOffer, Team team, User user) {

        TeamMembershipRequest request = new TeamMembershipRequest();
        request.setTeam(team);
        request.setUser(user);
        request.setRequestType(teamOffer.getRequestType());  // 팀에서 사용자에게 초대
        request.setStatus(RequestStatus.PENDING);
        request.setMessage(teamOffer.getMessage()); // 메시지가 있다면

        teamMembershipRequestRepository.save(request);
    }
}
