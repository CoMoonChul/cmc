package com.sw.cmc.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * packageName    : com.sw.cmc.common.advice
 * fileName       : CmcException
 * author         : ihw
 * date           : 2025. 1. 26.
 * description    :
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CmcException extends RuntimeException{
    public CmcException(String message) {
        super(message);
    }
}