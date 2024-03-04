package com.example.musinsaserver.product.application.port.in;

import com.example.musinsaserver.product.application.port.in.dto.ProductPriceUpdateRequest;

public interface UpdateProductUseCase {
    void updateProductPrice(final Long productId, final ProductPriceUpdateRequest productPriceUpdateRequest);
}
