package com.example.musinsaserver.common.dto;

public record ErrorResponse(String code, String message) {

    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    public static ErrorResponse internalServerError(final String message) {
        return new ErrorResponse(INTERNAL_SERVER_ERROR, message);
    }
}
