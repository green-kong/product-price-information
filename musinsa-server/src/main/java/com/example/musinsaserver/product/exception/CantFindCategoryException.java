package com.example.musinsaserver.product.exception;

import com.example.musinsaserver.common.exception.InternalServerError;

public class CantFindCategoryException extends InternalServerError {
    private static final String ERROR_CODE = "PRODUCT0006";
    private static final String FIXED_MESSAGE = "저장 된 product의 category를 찾을 수 없습니다. { product_id : %d, category_id: %d }";

    public CantFindCategoryException(final Long productId, final Long categoryId) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, productId, categoryId));
    }
}
