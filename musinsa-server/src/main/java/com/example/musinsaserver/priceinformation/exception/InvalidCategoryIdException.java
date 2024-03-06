package com.example.musinsaserver.priceinformation.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class InvalidCategoryIdException extends BadRequestException {
    private static final String ERROR_CODE = "PRICE_INFORMATION0004";
    private static final String FIXED_MESSAGE = "유효하지 않은 name id 입니다. { input_category_id : %d }";

    public InvalidCategoryIdException(final Long invalidCategoryId) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, invalidCategoryId));
    }
}
