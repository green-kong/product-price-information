package com.example.musinsaserver.product.application.port.in;

import com.example.musinsaserver.product.application.port.in.dto.ProductUpdateRequest;

public interface UpdateProductUseCase {
    void updateProduct(final Long productId, final ProductUpdateRequest productUpdateRequest);
}
