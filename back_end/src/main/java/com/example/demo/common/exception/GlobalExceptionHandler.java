package com.example.demo.common.exception;

import com.example.demo.common.response.ApiResponse;
import com.hazelcast.cp.lock.exception.LockAcquireLimitReachedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // BusinessException 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        ErrorCode code = ex.getErrorCode();
        return ResponseEntity
                .status(code.getStatus())
                .body(new ApiResponse<>(code.getStatus(), code.getMessage(), null));
    }

    // Valid 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST; // 커스텀 코드

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    String msg = error.getDefaultMessage();
                    if (msg.isBlank() || msg == null) {
                        msg = errorCode.getMessage();
                    }
                    return error.getField() + ": " + msg;
                })
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(errorCode.getStatus())
                .body(new ApiResponse<>(errorCode.getStatus(), errorMessage, null));
    }

    // db에러 처리
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataAccessException(DataAccessException ex) {
        ErrorCode code;

        if (ex instanceof org.springframework.dao.DuplicateKeyException) {
            code = ErrorCode.DUPLICATE_KEY_ERROR;
        } else {
            code = ErrorCode.DATABASE_ERROR;
        }

        return ResponseEntity.status(code.getStatus())
                .body(new ApiResponse<>(code.getStatus(), code.getMessage(), null));
    }

    // 네트워크 에러 처리
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ApiResponse<Void>> handleNetworkException(RestClientException ex) {
        ErrorCode code;

        Throwable cause = ex.getCause();

        if (cause instanceof java.net.SocketTimeoutException) {
            code = ErrorCode.NETWORK_TIMEOUT;
        } else if (cause instanceof java.net.ConnectException) {
            code = ErrorCode.NETWORK_CONNECT_FAIL;
        } else {
            code = ErrorCode.NETWORK_ERROR;
        }

        return ResponseEntity
                .status(code.getStatus())
                .body(new ApiResponse<>(code.getStatus(), code.getMessage(), null));
    }

    //구글 서버 에러 처리
    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleOAuth2Exception(OAuth2AuthenticationException ex) {
        String errorCode = ex.getError().getErrorCode();

        ErrorCode code;
        switch (errorCode) {
            case "invalid_token":
                code = ErrorCode.OAUTH2_INVALID_TOKEN;
                break;
            case "invalid_request":
                code = ErrorCode.OAUTH2_INVALID_REQUEST;
                break;
            case "access_denied":
                code = ErrorCode.OAUTH2_ACCESS_DENIED;
                break;
            default:
                code = ErrorCode.OAUTH2_PROVIDER_ERROR;
                break;
        }
        return ResponseEntity
                .status(code.getStatus())
                .body(new ApiResponse<>(code.getStatus(), code.getMessage(), null));
    }

    //hazel
    @ExceptionHandler(LockAcquireLimitReachedException.class)
    public ResponseEntity<ApiResponse<Void>> handleLockAcquireLimitReachedException(LockAcquireLimitReachedException e) {
        ErrorCode errorCode = ErrorCode.LOCK_ACQUIRE_FAILED;

        ApiResponse<Void> responseBody = new ApiResponse<>(
                errorCode.getStatus(),
                errorCode.getMessage(), // ErrorCode에 정의된 메시지를 사용
                null                  // 데이터 부분은 비워둡니다.
        );

        return ResponseEntity.status(errorCode.getStatus()).body(responseBody);
    }

    // 그외 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknownException(Exception ex) {
        // 공통 서버 내부 오류 코드 사용
        ErrorCode code = ErrorCode.INTERNAL_ERROR;
        return ResponseEntity
                .status(code.getStatus())
                .body(new ApiResponse<>(code.getStatus(), code.getMessage(), null));
    }
}