package com.example.musinsaserver.product.application.port.in.dto;

public record ProductUpdateRequest(int price, String category, Long brandId) {
}
