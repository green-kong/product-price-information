package com.example.musinsaserver.priceinformation.exception;

import com.example.musinsaserver.common.exception.InternalServerError;

public class NonExistentHighestInformation extends InternalServerError {

    private static final String ERROR_CODE = "PRICE_INFORMATION0008";
    private static final String FIXED_MESSAGE = "categoyId에 해당하는 최대 가격정보가 존재하지 않습니다. { category_id : %d }";

    public NonExistentHighestInformation(final Long categoryId) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, categoryId));
    }
}
