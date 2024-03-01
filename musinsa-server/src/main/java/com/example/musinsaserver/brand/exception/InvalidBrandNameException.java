package com.example.musinsaserver.brand.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class InvalidBrandNameException extends BadRequestException {
    private static final String ERROR_CODE = "BRAND0001";
    private static final String FIXED_MESSAGE = "상품의 이름은 한글자 이상이어야 합니다. { input_brand_name : %s }";

    public InvalidBrandNameException(final String inputBrandName) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, inputBrandName));
    }
}
