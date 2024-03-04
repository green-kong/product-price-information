package com.example.musinsaserver.priceinformation.exception;

import com.example.musinsaserver.common.exception.BadRequestException;

public class ProductIsNotDeletedException extends BadRequestException {
    private static final String ERROR_CODE = "PRICE_INFORMATION0003";
    private static final String FIXED_MESSAGE = "productId에 해당하는 product가 삭제 되지 않았습니다. { invalid_product_id : %d }";

    public ProductIsNotDeletedException(final Long notDeletedProductId) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, notDeletedProductId));
    }
}
