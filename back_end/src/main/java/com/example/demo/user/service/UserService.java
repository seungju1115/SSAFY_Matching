package com.example.demo.user.service;

import com.example.demo.user.dao.UserRepository;
import com.example.demo.user.dto.UserProfileRequest;
import com.example.demo.user.dto.UserProfileResponse;
import com.example.demo.user.dto.UserProfileUpdateRequest;
import com.example.demo.user.entity.User;
import com.example.demo.team.dao.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

        // 전체 및 부분 업데이트
        if (request.getUserName() != null) user.setUserName(request.getUserName());
        if (request.getUserProfile() != null) user.setUserProfile(request.getUserProfile());
        if (request.getMajor() != null) user.setMajor(request.getMajor());
        if (request.getLastClass() != null) user.setLastClass(request.getLastClass());
        if (request.getWantedPosition() != null) user.setWantedPosition(request.getWantedPosition());
        if (request.getProjectPref() != null) user.setProjectPref(request.getProjectPref());
        if (request.getPersonalPref() != null) user.setPersonalPref(request.getPersonalPref());
        if (request.getProjectExp() != null) user.setProjectExp(request.getProjectExp());
        if (request.getQualification() != null) user.setQualification(request.getQualification());
        if (request.getTechStack() != null) user.setTechStack(request.getTechStack());

        //if (request.getTeamId() != null) {
        //    Team team = teamRepository.findById(request.getTeamId())
        //            .orElseThrow(() -> new EntityNotFoundException("해당 팀이 존재하지 않습니다."));
        //    user.setTeam(team);
        //}

        return UserProfileResponse.toUserProfileResponse(user);
    }
}
