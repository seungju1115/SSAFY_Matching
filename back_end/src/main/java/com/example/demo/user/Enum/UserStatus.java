package com.example.demo.user.Enum;

public enum UserStatus {
    INACTIVE,    // 대기중이지 않음 (팀을 찾지 않는 상태)
    WAITING,     // 대기중 (팀을 찾고 있는 상태)
    IN_TEAM      // 팀 참여 (이미 팀에 소속된 상태)
}