package com.example.musinsaserver.product.application.port.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductPriceUpdateRequest(
        @Schema(description = "수정 할 가격", minimum = "10", maximum = "1_000_000")
        int price
) {
}
