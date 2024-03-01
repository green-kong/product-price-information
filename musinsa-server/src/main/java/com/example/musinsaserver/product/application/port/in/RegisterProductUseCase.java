package com.example.musinsaserver.product.application.port.in;

import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;

public interface RegisterProductUseCase {
    Long registerProduct(final RegisterProductRequest registerProductRequest);
}
