package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 모든 REST API 응답을 래핑하는 공통 DTO
 * @param <T> 실제 응답 데이터의 타입
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private int status;        // HTTP 상태 코드 (200, 201, ...)
    private String message;    // 사용자 메시지
    private T data;            // 실제 응답 데이터 (null 가능)

    /**
     * 요청 성공 - 200 OK
     */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "요청 성공", data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    /**
     * 생성 성공 - 201 Created
     */
    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "생성 성공", data);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(201, message, data);
    }

    /**
     * 삭제 성공 - 204 No Content (본문 없이 반환할 수 있음)
     */
    public static <T> ApiResponse<T> noContent() {
        return new ApiResponse<>(204, "삭제 성공", null);
    }

    /**
     * 잘못된 요청 - 400 Bad Request
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, message, null);
    }

    /**
     * 인증 실패 - 401 Unauthorized
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(401, message, null);
    }

    /**
     * 권한 없음 - 403 Forbidden
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(403, message, null);
    }

    /**
     * 자원 없음 - 404 Not Found
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, message, null);
    }

    /**
     * 서버 오류 - 500 Internal Server Error
     */
    public static <T> ApiResponse<T> internalServerError(String message) {
        return new ApiResponse<>(500, message, null);
    }
}