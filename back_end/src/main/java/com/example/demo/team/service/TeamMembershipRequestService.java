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
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        IMap<String,String> lock = hazelcastInstance.getMap("teamMembershipRequestLock");
        boolean lockAcquired = false;
        try {
            lockAcquired=lock.tryLock(key,5,TimeUnit.SECONDS);
            if (!lockAcquired) {
                throw new LockAcquireLimitReachedException("요청이 처리 중입니다. 잠시 후 다시 시도해주세요.");
            }

            boolean exists = team.getMembershipRequests().stream()
                    .anyMatch(req -> req.getUser().equals(user) && req.getStatus() != RequestStatus.REJECTED);

            if (exists) {
                throw new BusinessException(ErrorCode.TEAM_REQUEST_ALLREADY_EXIST);
            }
            saveTeamOffer(teamOffer, team, user);
        } catch (InterruptedException | LockAcquisitionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("요청 처리가 중단되었습니다.", e);
        }
        finally {
            if (lockAcquired) lock.unlock(key);
        }

        saveTeamOffer(teamOffer, team, user);


        messagingTemplate.convertAndSend("/queue/team/offer/" + teamOffer.getUserId(), teamOffer.getMessage());
    }
    @Transactional
    public void requestMemberToTeam(TeamOffer teamOffer) {
        Team team = teamRepository.findById(teamOffer.getTeamId()).orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
        User user = userRepository.findById(teamOffer.getUserId()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String key = team.getId() + "+" + user.getId();
        IMap<String, String> lock = hazelcastInstance.getMap("teamMembershipRequestLock");
        boolean lockAcquired = false;
        try {
            lockAcquired = lock.tryLock(key, 5, TimeUnit.SECONDS);
            if (!lockAcquired) {
                throw new LockAcquireLimitReachedException("요청이 처리 중입니다. 잠시 후 다시 시도해주세요.");
            }

            boolean exists = team.getMembershipRequests().stream()
                    .anyMatch(req -> req.getTeam().equals(team) && req.getStatus() != RequestStatus.REJECTED);
            if (exists) {
                throw new BusinessException(ErrorCode.TEAM_REQUEST_ALLREADY_EXIST);
            }
            saveTeamOffer(teamOffer, team, user);
        } catch (InterruptedException | LockAcquisitionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("요청 처리가 중단되었습니다.", e);
        }
        finally {
            if (lockAcquired) lock.unlock(key);
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

    public List<TeamMembershipResponse> getAllRequest(Long teamId) {

        List<TeamMembershipRequest> requests = teamMembershipRequestRepository.findAllByTeamId(teamId);

        return requests.stream()
                .map(TeamMembershipResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
