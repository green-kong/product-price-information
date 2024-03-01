package com.example.musinsaserver.product.application.port.in.dto;

import com.example.musinsaserver.product.domain.Product;

public record RegisterProductRequest(int price, Long brandId) {

    public Product toProduct() {
        return Product.createWithoutId(price, brandId);
    }
}