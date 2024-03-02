package com.example.musinsaserver.product.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class InvalidCategoryException extends BadRequestException {
    private static final String ERROR_CODE = "PRODUCT0001";
    private static final String FIXED_MESSAGE = "일치하는 카테고리가 없습니다. { input_category : %s}";

    public InvalidCategoryException(final String inputCategory) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, inputCategory));
    }
}
