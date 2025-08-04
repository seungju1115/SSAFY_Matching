package com.example.demo.team.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀 요청 타입", enumAsRef = true)
public enum RequestType {
    @Schema(description = "사용자가 팀에 가입 요청")
    JOIN_REQUEST,

    @Schema(description = "팀에서 사용자에게 초대 요청")
    INVITE
}