package com.example.musinsaserver.priceinformation.exception;

import com.example.musinsaserver.common.exception.InternalServerError;

public class NonExistentProductWithBrandIdAndCategoryId extends InternalServerError {
    private static final String ERROR_CODE = "PRICE_INFORMATION0007";
    private static final String FIXED_MESSAGE = "categoryId와 brandId에 해당하는 product가 존재하지 않습니다. { category_id : %d, brand_id: %d }";

    public NonExistentProductWithBrandIdAndCategoryId(final Long brandId, final Long categoryId) {
        super(ERROR_CODE, String.format(FIXED_MESSAGE, brandId, categoryId));
    }
}
