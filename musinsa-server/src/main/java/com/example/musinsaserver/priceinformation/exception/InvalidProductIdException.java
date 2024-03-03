package com.example.musinsaserver.priceinformation.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class InvalidProductIdException extends BadRequestException {
    private static final String ERROR_CODE = "PRICE_INFORMATION0001";
    private static final String FIXED_MESSAGE = "프로덕트 id가 유효하지 않습니다. { input_product_id : %d }";

    public InvalidProductIdException(final Long invalidProductId) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, invalidProductId));
    }
}
