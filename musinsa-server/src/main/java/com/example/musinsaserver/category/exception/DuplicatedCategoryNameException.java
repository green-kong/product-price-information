package com.example.musinsaserver.category.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class DuplicatedCategoryNameException extends BadRequestException {
    private static final String ERROR_CODE = "CATEGORY0001";
    private static final String FIXED_MESSAGE = "이미 존재하는 카테고리 이름입니다. { %s }";

    public DuplicatedCategoryNameException(final String duplicatedCategoryName) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, duplicatedCategoryName));
    }
}
