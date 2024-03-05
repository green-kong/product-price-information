package com.example.musinsaserver.priceinformation.application.port.in;

public interface MinimumPriceUpdateUseCase {
    void updateMinimumPrice(final Long brandId, final Long categoryId);
}
