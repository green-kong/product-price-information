package com.example.musinsaserver.product.application.port.in.dto;

import com.example.musinsaserver.product.domain.Product;

public record ProductResponse(Long id, int price, String brand, String category) {
    public static ProductResponse of(final Product product, final String brand, final String category) {
        return new ProductResponse(
                product.getId(),
                product.getPriceValue(),
                brand,
                category
        );
    }
}
