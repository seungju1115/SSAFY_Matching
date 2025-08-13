package com.example.demo.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 모든 REST API 응답을 래핑하는 공통 DTO
 * @param <T> 실제 응답 데이터의 타입
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private int status;     // HTTP 상태 코드
    private String message; // 사용자 메시지
    private T data;         // 응답 데이터 (null 가능)

    // ✅ 기본 메시지 상수 (불변)
    private static final String OK_BASIC_MESSAGE = "요청 성공";
    private static final String CREATED_BASIC_MESSAGE = "생성 성공";
    private static final String DELETED_BASIC_MESSAGE = "삭제 성공";

    /**
     * 200 OK
     */
    public static <T> ApiResponse<T> ok() {
        return ok(OK_BASIC_MESSAGE, null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ok(OK_BASIC_MESSAGE, data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), message, data);
    }

    /**
     * 201 Created
     */
    public static <T> ApiResponse<T> created() {
        return created(CREATED_BASIC_MESSAGE, null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return created(CREATED_BASIC_MESSAGE, data);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), message, data);
    }

    /**
     * 204 No Content
     */
    public static <T> ApiResponse<T> noContent() {
        return new ApiResponse<>(HttpStatus.NO_CONTENT.value(), DELETED_BASIC_MESSAGE, null);
    }
}