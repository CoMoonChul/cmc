package com.sw.cmc.common.advice;

/**
 * packageName    : com.sw.cmc.common.advice
 * fileName       : ErrorResponse
 * author         : ihw
 * date           : 2025. 2. 27.
 * description    : 에러 발생 시 클라이언트 반환 객체
 */
public class ErrorResponse {
    private final String errorCode;
    private final String message;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
