package com.example.demo.user.service;

import com.example.demo.team.entity.Team;
import com.example.demo.user.Enum.ProjectGoalEnum;
import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.dto.*;
import com.example.demo.user.dto.UserSearchRequest;
import com.example.demo.user.entity.User;
import com.example.demo.team.dao.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Transactional
    public UserProfileResponse getProfile(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return UserProfileResponse.toUserProfileResponse(user);
    }

    @Cacheable(value = "longTermCache", key = "'user:'+ #id")
    @Transactional
    public UserProfileResponse getProfile(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        return UserProfileResponse.toUserProfileResponse(user);
    }

    @Transactional
    public UserProfileResponse saveProfile(UserProfileRequest userProfileRequest){
        User user = UserProfileRequest.toEntity(userProfileRequest);
        user = userRepository.save(user);
        return UserProfileResponse.toUserProfileResponse(user);
    }

    @CacheEvict(value = "longTermCache", key = "'user:'+ #id")
    @Transactional
    public void deleteProfile(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저가 없습니다."));
        userRepository.delete(user);
    }

    @CacheEvict(value = "longTermCache", key = "'user:'+ #id")
    @Transactional
    public UserProfileResponse updateUserProfile(UserProfileUpdateRequest request, Long id) {
        System.out.println("유저 찾기 전");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        // 전체 및 부분 업데이트
        if (request.getUserName() != null) {
            user.setUserName(request.getUserName());
        }
        if (request.getUserProfile() != null) user.setUserProfile(request.getUserProfile());
        if (request.getMajor() != null) user.setMajor(request.getMajor());
        if (request.getLastClass() != null) user.setLastClass(request.getLastClass());
        if (request.getWantedPosition() != null) user.setWantedPosition(request.getWantedPosition());
        if (request.getProjectGoal() != null) user.setProjectGoal(request.getProjectGoal());
        if (request.getProjectVive() != null) user.setProjectVive(request.getProjectVive());
        if (request.getProjectExp() != null) user.setProjectExp(request.getProjectExp());
        if (request.getQualification() != null) user.setQualification(request.getQualification());
        if (request.getTechStack() != null) user.setTechStack(request.getTechStack());

        if (request.getTeamId() != null) {
            Team team = teamRepository.findById(request.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 팀이 존재하지 않습니다."));
            user.setTeam(team);
        }

        return UserProfileResponse.toUserProfileResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserSearchResponse> searchUsersWithoutTeam(UserSearchRequest request) {
        // 조건에 맞는 유저가 없으면 빈 리스트 반환하므로 null이 될 수 없음.
        List<User> users = userRepository.findUsersWithoutTeamByFilters(
                request.getMajor(),
                request.getWantedPosition(),
                request.getTechStack(),
                request.getProjectVive(),
                request.getProjectGoal());
        
        return users.stream()
                .map(UserSearchResponse::fromUser)
                .collect(java.util.stream.Collectors.toList());
    }
    
    private boolean matchesTechStack(User user, java.util.Set<com.example.demo.user.Enum.TechEnum> techStack) {
        if (techStack == null || techStack.isEmpty()) {
            return true;
        }
        if (user.getTechStack() == null) {
            return false;
        }
        return user.getTechStack().stream().anyMatch(techStack::contains);
    }
    
    private boolean matchesProjectPref(User user, java.util.Set<ProjectGoalEnum> projectPref) {
        if (projectPref == null || projectPref.isEmpty()) {
            return true;
        }
        if (user.getProjectGoal() == null) {
            return false;
        }
        return user.getProjectGoal().stream().anyMatch(projectPref::contains);
    }
}
