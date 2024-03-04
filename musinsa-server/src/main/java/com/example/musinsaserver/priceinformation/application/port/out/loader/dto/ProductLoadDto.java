package com.example.musinsaserver.priceinformation.application.port.out.loader.dto;

public record ProductLoadDto(Long productId, Long brandId, int price, Long categoryId) {
}
