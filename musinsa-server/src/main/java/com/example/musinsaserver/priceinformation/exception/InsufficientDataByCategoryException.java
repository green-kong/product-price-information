package com.example.musinsaserver.priceinformation.exception;

import com.example.musinsaserver.common.exception.InternalServerError;

public class InsufficientDataByCategoryException extends InternalServerError {
    private static final String ERROR_CODE = "PRICE_INFORMATION0006";
    private static final String FIXED_MESSAGE = "최소가격정보가 존재하지 않는 카테고리가 존재합니다. { should_be_count: %s, current_count: %s }";

    public InsufficientDataByCategoryException(final int shouldBeCount, final int currentCount) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, shouldBeCount, currentCount));
    }
}
