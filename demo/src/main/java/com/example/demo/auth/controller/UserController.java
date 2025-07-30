package com.example.demo.auth.controller;

import com.example.demo.auth.dto.UserProfileRequest;
import com.example.demo.auth.dto.UserProfileResponse;
import com.example.demo.auth.dto.UserProfileUpdateRequest;
import com.example.demo.auth.service.UserService;
import com.example.demo.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/profile")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse> getProfileWithoutId(){
        UserProfileResponse userProfileResponse = userService.getProfile();
        return ResponseEntity.ok(ApiResponse.ok(userProfileResponse));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getProfileWithId(@PathVariable Long id){
        UserProfileResponse userProfileResponse = userService.getProfile(id);
        return ResponseEntity.ok(ApiResponse.ok(userProfileResponse));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createProfile(@RequestBody UserProfileRequest userProfileRequest){
        UserProfileResponse userProfileResponse = userService.saveProfile(userProfileRequest);
        return ResponseEntity.ok(ApiResponse.ok(userProfileResponse));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteProfile(@PathVariable Long id){
        userService.deleteProfile(id);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse> updateProfile(@RequestBody UserProfileUpdateRequest  userProfileUpdateRequest, @PathVariable Long id){
        UserProfileResponse userProfileResponse = userService.updateUserProfile(userProfileUpdateRequest, id);
        return ResponseEntity.ok(ApiResponse.ok(userProfileResponse));
    }
}
