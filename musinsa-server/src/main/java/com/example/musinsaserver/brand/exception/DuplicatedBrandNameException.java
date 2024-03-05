package com.example.musinsaserver.brand.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class DuplicatedBrandNameException extends BadRequestException {
    private static final String ERROR_CODE = "BRAND0002";
    private static final String FIXED_MESSAGE = "이미 존재하는 브랜드 명입니다. { duplicated_brand_name : %s }";

    public DuplicatedBrandNameException(final String inputBrandName) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, inputBrandName));
    }
}
