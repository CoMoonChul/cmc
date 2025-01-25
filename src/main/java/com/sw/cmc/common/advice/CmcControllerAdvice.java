package com.sw.cmc.common.advice;

import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * packageName    : com.sw.cmc.common.advice
 * fileName       : TempAdvice
 * author         : IHW
 * date           : 2025-01-24
 * description    :
 */
@ControllerAdvice
public class CmcControllerAdvice {
    /**
     * methodName : handleNoSuchMessageException
     * author : IM HYUN WOO
     * description : 존재하지 않는 에러 코드 사용 시 발생 > messages.properties 확인 필요
     *
     * @param ex
     * @return response entity
     */
    @ExceptionHandler(NoSuchMessageException.class)
    public ResponseEntity<String> handleNoSuchMessageException(NoSuchMessageException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("[MSG0000] 메시지 코드를 찾을 수 없습니다.");
    }

    /**
     * methodName : handleStashException
     * author : IM HYUN WOO
     * description :
     *
     * @param e
     * @return response entity
     */
    @ExceptionHandler(CmcException.class)
    public ResponseEntity<String> handleStashException(CmcException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}