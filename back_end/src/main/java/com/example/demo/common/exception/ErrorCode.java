package com.example.demo.common.exception;

import lombok.Getter;


@Getter
public enum ErrorCode {

    // ==================== User 관련 에러 ====================
    USER_NOT_FOUND(404, "해당 사용자를 찾을 수 없습니다."),
    USER_NOT_IN_TEAM(400, "팀에 소속되지 않았습니다."),
    USER_FORBIDDEN(403, "사용자 권한이 없습니다."),
    USER_DUPLICATE_EMAIL(400, "이미 존재하는 이메일입니다."),
    USER_PASSWORD_INVALID(400, "비밀번호가 올바르지 않습니다."),
    USER_UNAUTHORIZED(401, "인증이 필요합니다."),
    USER_ALLREADY_HAS_TEAM(400, "소속된 팀이 이미 존재합니다."),

    // ==================== Team 관련 에러 ====================
    TEAM_NOT_FOUND(404, "해당 팀을 찾을 수 없습니다."),
    TEAM_ALLREADY_LOCKED(400, "이미 고정된 팀입니다."),
    TEAM_INVALID_REQUEST(400, "팀 요청이 유효하지 않습니다."),
    TEAM_NAME_ALREADY_EXISTS(400, "이미 존재하는 팀 이름입니다."),
    TEAM_MEMBER_LIMIT_EXCEEDED(400, "팀 인원 제한을 초과했습니다."),
    TEAM_ACCESS_DENIED(403, "팀 접근 권한이 없습니다."),
    TEAM_REQUEST_ALLREADY_EXIST(400, "팀 초대 요청이 이미 존재합니다."),
//    TEAM_REQUEST_ALLREADY_EXIST(400, "팀 초대 요청이 이미 존재합니다."),

    // ==================== Chat 관련 에러 ====================
    CHAT_ROOM_NOT_FOUND(404, "채팅방을 찾을 수 없습니다."),
    CHATROOM_MEMBER_ALREADY_EXISTS(400, "이미 채팅방에 참여한 사용자입니다."),
    CHATROOM_MEMBER_NOT_FOUND(404, "채팅방에서 찾을 수 없는 멤버입니다."),
    INVALID_PRIVATEROOM_REQUEST(400, "userId1, userId2 는 필수 입력 사항입니다."),
    INVALID_CHAT_ROOM_TYPE(400, "유효하지 않은 채팅방 타입입니다."),

    // ==================== 공통 에러 ====================
    INVALID_REQUEST(400, "요청이 유효하지 않습니다."),
    INTERNAL_ERROR(500, "서버 내부 오류입니다."),

    DATABASE_ERROR(500, "데이터베이스 처리 중 오류가 발생했습니다."),
    DUPLICATE_KEY_ERROR(409, "중복된 키가 존재합니다."),

    NETWORK_TIMEOUT(504, "네트워크 연결 시간 초과입니다."),
    NETWORK_CONNECT_FAIL(503, "네트워크 연결에 실패했습니다."),
    NETWORK_ERROR(500, "네트워크 오류가 발생했습니다."),
    LOCK_ACQUIRE_FAILED(409, "현재 다른 요청을 처리 중입니다. 잠시 후 다시 시도해주세요."),

    // ==================== 구글 에러 =====================
    OAUTH2_INVALID_TOKEN(401, "유효하지 않은 액세스 토큰입니다."),
    OAUTH2_INVALID_REQUEST(400, "잘못된 OAuth2 요청입니다."),
    OAUTH2_ACCESS_DENIED(403, "OAuth2 접근 권한이 거부되었습니다."),
    OAUTH2_PROVIDER_ERROR(502, "외부 인증 제공자 서비스 오류입니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}