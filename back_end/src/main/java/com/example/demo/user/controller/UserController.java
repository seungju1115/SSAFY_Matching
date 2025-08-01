package com.example.demo.user.controller;

import com.example.demo.user.dto.SearchUserRequest;
import com.example.demo.user.dto.SearchUserResponse;
import com.example.demo.user.dto.UserProfileRequest;
import com.example.demo.user.dto.UserProfileResponse;
import com.example.demo.user.dto.UserProfileUpdateRequest;
import com.example.demo.user.service.UserService;
import com.example.demo.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponse> updateProfile(@RequestBody UserProfileUpdateRequest userProfileUpdateRequest, @PathVariable Long id){
        UserProfileResponse userProfileResponse = userService.updateUserProfile(userProfileUpdateRequest, id);
        return ResponseEntity.ok(ApiResponse.ok(userProfileResponse));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse> searchUsersWithoutTeam(@RequestBody SearchUserRequest request) {
        List<SearchUserResponse> users = userService.searchUsersWithoutTeam(request);
        return ResponseEntity.ok(ApiResponse.ok(users));
    }
}
