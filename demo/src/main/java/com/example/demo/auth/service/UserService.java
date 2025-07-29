package com.example.demo.auth.service;

import com.example.demo.auth.dao.UserRepository;
import com.example.demo.auth.dto.UserProfileResponse;
import com.example.demo.auth.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getProfile(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.getTeam().addUser(user);
        return user;
    }
}
