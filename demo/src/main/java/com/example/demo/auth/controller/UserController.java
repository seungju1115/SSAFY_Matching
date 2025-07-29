package com.example.demo.auth.controller;

import com.example.demo.auth.entity.User;
import com.example.demo.auth.service.UserService;
import com.example.demo.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/users/profile")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long id){
        User user = userService.getProfile(id);
        return ResponseEntity.ok(ApiResponse.ok(user));
    }
}
