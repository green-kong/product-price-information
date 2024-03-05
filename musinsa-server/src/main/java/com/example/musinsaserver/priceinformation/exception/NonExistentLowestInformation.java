package com.example.musinsaserver.priceinformation.exception;

import com.example.musinsaserver.common.exception.InternalServerError;

public class NonExistentLowestInformation extends InternalServerError {
    private static final String ERROR_CODE = "PRICE_INFORMATION0009";
    private static final String FIXED_MESSAGE = "categoyId에 해당하는 최소 가격정보가 존재하지 않습니다. { category_id : %d }";

    public NonExistentLowestInformation(final Long categoryId) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, categoryId));
    }
}
