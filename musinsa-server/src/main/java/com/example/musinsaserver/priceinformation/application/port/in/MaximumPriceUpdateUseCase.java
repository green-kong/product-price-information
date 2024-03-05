package com.example.musinsaserver.priceinformation.application.port.in;

public interface MaximumPriceUpdateUseCase {
    void updateMaximumPrice(final Long brandId, final Long categoryId);
}
