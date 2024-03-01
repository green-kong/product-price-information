package com.example.musinsaserver.product.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class InvalidPriceException extends BadRequestException {
    private static final String ERROR_CODE = "PRODUCT0001";
    private static final String FIXED_MESSAGE = "가격은 10원이상 1,000,000원 이하여야 합니다. { input_invalid_price : %d}";

    public InvalidPriceException(final int inputInvalidPrice) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, inputInvalidPrice));
    }
}
