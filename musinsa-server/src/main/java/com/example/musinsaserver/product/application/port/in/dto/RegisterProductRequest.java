package com.example.musinsaserver.product.application.port.in.dto;

import com.example.musinsaserver.product.domain.Product;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterProductRequest(
        @Schema(description = "프로덕트 가격", minimum = "10",maximum = "1_000_000")
        int price,
        Long categoryId,
        Long brandId
) {

    public Product toProduct() {
        return Product.createWithoutId(price, categoryId, brandId);
    }
}
