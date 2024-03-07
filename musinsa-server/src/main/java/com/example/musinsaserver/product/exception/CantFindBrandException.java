package com.example.musinsaserver.product.exception;

import com.example.musinsaserver.common.exception.InternalServerError;

public class CantFindBrandException extends InternalServerError {
    private static final String ERROR_CODE = "PRODUCT0005";
    private static final String FIXED_MESSAGE = "저장 된 product의 brand를 찾을 수 없습니다. { product_id : %d, brand_id: %d }";

    public CantFindBrandException(final Long productId, final Long brandId) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, productId, brandId));
    }
}
