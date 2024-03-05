package com.example.musinsaserver.priceinformation.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class NonExistentCategoryNameException extends BadRequestException {
    private static final String ERROR_CODE = "PRICE_INFORMATION0001";
    private static final String FIXED_MESSAGE = "존재하지 않는 카테고리 입니다. { invalid_category : %s }";

    public NonExistentCategoryNameException(final String invalidCategory) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, invalidCategory));
    }
}
