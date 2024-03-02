package com.example.musinsaserver.product.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class NonExistentBrandException extends BadRequestException {
    private static final String ERROR_CODE = "PRODUCT0003";
    private static final String FIXED_MESSAGE = "프로덕트의 브랜드가 존재하지 않습니다. { input_brand_id : %d }";

    public NonExistentBrandException(final Long inputBrandId) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, inputBrandId));
    }
}
