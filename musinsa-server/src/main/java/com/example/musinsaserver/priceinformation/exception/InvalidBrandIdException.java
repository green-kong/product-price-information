package com.example.musinsaserver.priceinformation.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class InvalidBrandIdException extends BadRequestException {
    private static final String ERROR_CODE = "PRICE_INFORMATION0002";
    private static final String FIXED_MESSAGE = "유효하지 않은 브랜드 id 입니다. { input_brand_id : %d }";

    public InvalidBrandIdException(final Long invalidBrandId) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, invalidBrandId));
    }
}
