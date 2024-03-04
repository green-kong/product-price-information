package com.example.musinsaserver.category.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class InvalidCategoryNameException extends BadRequestException {
    private static final String ERROR_CODE = "CATEGORY0001";
    private static final String FIXED_MESSAGE = "카테고리명은 2글자 이상이어야 합니다. { invalidNameValue : %s }";

    public InvalidCategoryNameException(final String invalidNameValue) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, invalidNameValue));
    }
}
