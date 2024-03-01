package com.example.musinsaserver.common.exception;

public abstract class BadRequestException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    protected BadRequestException(final String errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
