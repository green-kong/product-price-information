package com.example.musinsaserver.product.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class NonExistentProductException extends BadRequestException {
    private static final String ERROR_CODE = "PRODUCT0004";
    private static final String FIXED_MESSAGE = "존재하지 않는 프로덕트입니다. { input_product_id : %d }";

    public NonExistentProductException(final Long invalidProductId) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, invalidProductId));
    }
}
