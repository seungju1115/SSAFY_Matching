package com.example.demo.common.response;

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
}