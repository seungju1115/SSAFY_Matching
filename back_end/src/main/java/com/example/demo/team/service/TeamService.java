package com.example.demo.team.service;

import com.example.demo.chat.dao.ChatRoomMemberRepository;
import com.example.demo.chat.entity.ChatRoomMember;
import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.team.entity.TeamStatus;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.dto.UserDetailResponse;
import com.example.demo.user.entity.User;
import com.example.demo.chat.dto.ChatRoomRequest;
import com.example.demo.chat.entity.RoomType;
import com.example.demo.chat.service.ChatRoomService;
import com.example.demo.team.dao.TeamRepository;
import com.example.demo.team.dto.*;
import com.example.demo.team.entity.Team;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {
    private static final Logger log = LoggerFactory.getLogger(TeamService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    // 1. 팀 생성
    @Transactional
    public TeamDetailResponse createTeam(TeamRequest dto) { // 팀장만 생성 가능
        // 팀장 조회
        User leader = userRepository.findById(dto.getLeaderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // ✅ 이미 팀에 소속되어 있는지 확인
        if (leader.getTeam() != null) throw new BusinessException(ErrorCode.USER_ALLREADY_HAS_TEAM);

        // 팀 객체 생성
        Team team = new Team();
        team.setLeader(leader);
        leader.setTeam(team);
        team = toTeam(dto,team);

        // 팀장도 팀의 멤버로 설정 (양방향 메서드 이용)
//        leader.setTeam(team); // 내부적으로 team.getMembers().add(this) 포함됨

        // 저장
        Team saved = teamRepository.save(team);

        ChatRoomRequest chatRoomRequest = new ChatRoomRequest();
        chatRoomRequest.setRoomType(RoomType.TEAM);
        chatRoomRequest.setTeamId(saved.getId());
        chatRoomRequest.setUserId(dto.getLeaderId());

        chatRoomService.createTeamChatRoom(chatRoomRequest);

        return teamToResponse(saved);
    }

    // 2. 전체 팀 조회
    public List<TeamDetailResponse> getAllTeams() {
        return teamRepository.findAllWithDetails().stream()
                .map(this::teamToResponse) // 헬퍼 메서드를 참조하여 매핑
                .collect(Collectors.toList());
    }

    // 3. 팀 조건 조회
    public List<TeamDetailResponse> searchConditionTeam(TeamSearchRequest teamRequest) {
        return teamRepository.findAllWithDetails().stream()
                // teamName 필터링
                .filter(team -> teamRequest.getTeamName() == null || team.getTeamName().contains(teamRequest.getTeamName()))
                // leaderId 필터링
                .filter(team -> teamRequest.getLeaderId() == null ||
                        (team.getLeader() != null && team.getLeader().getId().equals(teamRequest.getLeaderId())))
                // teamToResponse 헬퍼 메서드를 사용하여 DTO로 변환
                .map(this::teamToResponse)
                .collect(Collectors.toList());
    }

    // 4. 팀 정보 조회
//    @Cacheable(value = "longTermCache", key = "'team:' + #teamId") // 유저 프로필 업데이트 후 동기화 안 됨.
    public TeamDetailResponse getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));

        return teamToResponse(team);
    }

    // 5. 팀 정보 삭제
    @Transactional
    @CacheEvict(value = "longTermCache", key = "'team:' + #teamId")
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));

        // 멤버들의 team 참조 해제 (연관관계 주인: User.team)
        if (team.getMembers() != null) {
            for (User member : new ArrayList<>(team.getMembers())) {
                member.setTeam(null); // 안전하게 순회
            }
        }

        teamRepository.delete(team);
    }

    // 6. 팀 정보 수정
    @Transactional
    @CacheEvict(value = "longTermCache", key = "'team:' + #teamId")
    public TeamDetailResponse modifyTeam(TeamRequest teamRequest) {
        Team team = teamRepository.findById(teamRequest.getTeamId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));

        //팀명 중복 시
        teamRepository.findByTeamName(teamRequest.getTeamName())
                .ifPresent(team2 -> {
                    // 이미 존재하는 팀입니다
                    throw new BusinessException(ErrorCode.TEAM_NAME_ALREADY_EXISTS);
                });

        //변경 사항 적용
        team = toTeam(teamRequest, team);
        
        User newLeader = userRepository.findById(teamRequest.getLeaderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        team.setLeader(newLeader);

        return teamToResponse(team);
    }

    // 7. 팀 멤버 초대
    public TeamDetailResponse inviteMemberTeam(TeamInviteRequest teamInviteRequest) {
        User invitedUser = userRepository.findById(teamInviteRequest.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Team team = teamRepository.findById(teamInviteRequest.getTeamId())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));

        // 이미 팀에 소속된 사용자라면 예외 처리
        if (invitedUser.getTeam() != null && invitedUser.getTeam().getId().equals(team.getId())) {
            throw new BusinessException(ErrorCode.USER_ALLREADY_HAS_TEAM);
        }

        invitedUser.setTeam(team);

        // ✅ 팀 채팅방에 자동 추가
        ChatRoomRequest chatRoomRequest = new ChatRoomRequest();
        chatRoomRequest.setRoomId(team.getChatRoom().getId());
        chatRoomRequest.setUserId(teamInviteRequest.getUserId());

        chatRoomService.addMemberToTeamChatRoom(chatRoomRequest);

        return teamToResponse(team);
    }

    // 6. 팀 정보 수정
    @Transactional
    @CacheEvict(value = "longTermCache", key = "'team:' + #teamId")
    public void lockTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));

        if(team.getStatus() != TeamStatus.LOCKED)team.setStatus(TeamStatus.LOCKED);
        else throw new BusinessException(ErrorCode.TEAM_ALLREADY_LOCKED);
    }

    @Transactional
    public void leaveTeam(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        log.info("User 영속성: {}", entityManager.contains(user));
        log.info("User chatRoomMembers size: {}", user.getChatRoomMembers().size());

        Team team = user.getTeam();
        if (team == null) {
            throw new BusinessException(ErrorCode.USER_NOT_IN_TEAM);
        }
        log.info("Team 영속성: {}", entityManager.contains(team));

        ChatRoomMember member = chatRoomMemberRepository.findByUserIdAndChatRoomId(userId, team.getChatRoom().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHATROOM_MEMBER_NOT_FOUND));

        log.info("Member 영속성: {}", entityManager.contains(member));
        log.info("Member.user 영속성: {}", member.getUser() != null ? entityManager.contains(member.getUser()) : "null");

        // 연관관계 끊기 전 상태
        log.info("Before unlink user.chatRoomMembers.size: {}", user.getChatRoomMembers().size());

        member.setUser(null);
        member.setChatRoom(null);

        // 끊은 후 user의 chatRoomMembers 상태 출력
        log.info("After unlink user.chatRoomMembers.size: {}", user.getChatRoomMembers().size());

        

        // user와 team 연결 끊기
        user.setTeam(null);
        log.info("User before saveAndFlush: {}", user.getId());
        log.info("User is managed by EntityManager before saveAndFlush: {}", entityManager.contains(user));
        userRepository.saveAndFlush(user);

        // 최종 영속성 상태 확인
        log.info("최종 User 영속성: {}", entityManager.contains(user));
    }

    public Team toTeam(TeamRequest teamRequest,Team team) {
        team.setTeamName(teamRequest.getTeamName());
        team.setTeamDomain(teamRequest.getTeamDomain());
        team.setTeamVive(new HashSet<>(teamRequest.getTeamVive()));
        team.setTeamPreference(new HashSet<>(teamRequest.getTeamPreference()));
        team.setBackendCount(teamRequest.getBackendCount());
        team.setFrontendCount(teamRequest.getFrontendCount());
        team.setAiCount(teamRequest.getAiCount());
        team.setPmCount(teamRequest.getPmCount());
        team.setDesignCount(teamRequest.getDesignCount());
        team.setTeamDescription(teamRequest.getTeamDescription());
        return team;
    }

    public TeamDetailResponse teamToResponse(Team team) {
        TeamDetailResponse response = new TeamDetailResponse();

        response.setTeamId(team != null ? team.getId() : null);

        if (team != null && team.getChatRoom() != null) {
            response.setChatRoomId(team.getChatRoom().getId());
        } else {
            response.setChatRoomId(null);
        }

        response.setTeamName(team != null ? team.getTeamName() : null);
        response.setTeamDomain(team != null ? team.getTeamDomain() : null);

        if (team != null && team.getTeamVive() != null) {
            response.setTeamVive(team.getTeamVive());
        } else {
            response.setTeamVive(null);
        }

        if (team != null && team.getTeamPreference() != null) {
            response.setTeamPreference(team.getTeamPreference());
        } else {
            response.setTeamPreference(null);
        }

        response.setBackendCount(team != null ? team.getBackendCount() : 0);
        response.setFrontendCount(team != null ? team.getFrontendCount() : 0);
        response.setAiCount(team != null ? team.getAiCount() : 0);
        response.setPmCount(team != null ? team.getPmCount() : 0);
        response.setDesignCount(team != null ? team.getDesignCount() : 0);

        response.setTeamDescription(team != null ? team.getTeamDescription() : null);

        if (team != null && team.getLeader() != null) {
            response.setLeader(UserDetailResponse.fromEntity(team.getLeader()));
        } else {
            response.setLeader(null);
        }

        List<UserDetailResponse> members = new ArrayList<>();
        if (team != null && team.getMembers() != null) {
            for (User user : team.getMembers()) {
                if (user != null) {
                    members.add(UserDetailResponse.fromEntity(user));
                }
            }
        }
        response.setMembers(members);
        response.setTeamStatus(team.getStatus());
        return response;
    }

}
