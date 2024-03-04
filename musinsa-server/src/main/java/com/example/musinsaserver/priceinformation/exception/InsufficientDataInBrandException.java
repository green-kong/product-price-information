package com.example.musinsaserver.priceinformation.exception;

import com.example.musinsaserver.common.exception.InternalServerError;

public class InsufficientDataInBrandException extends InternalServerError {
    private static final String ERROR_CODE = "PRICE_INFORMATION0005";
    private static final String FIXED_MESSAGE = "해당 브랜드의 상품 데이터가 충분하지 않습니다. { brand_id : %s, should_be_count: %s, current_count: %s }";

    public InsufficientDataInBrandException(final Long brandId, final int shouldBeCount, final int currentCount) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, brandId, shouldBeCount, currentCount));
    }
}
