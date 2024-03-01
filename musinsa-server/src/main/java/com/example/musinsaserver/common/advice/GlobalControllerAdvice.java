package com.example.musinsaserver.common.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.musinsaserver.common.dto.ErrorResponse;
import com.example.musinsaserver.common.exception.BadRequestException;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException exception) {
        final ErrorResponse errorResponse = new ErrorResponse(exception.getErrorCode(), exception.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUnExpectedException(final Exception exception) {
        return ResponseEntity.internalServerError().body(ErrorResponse.internalServerError(exception.getMessage()));
    }
}
