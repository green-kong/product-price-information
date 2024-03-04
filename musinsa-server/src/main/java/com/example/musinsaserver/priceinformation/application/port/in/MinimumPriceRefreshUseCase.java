package com.example.musinsaserver.priceinformation.application.port.in;

public interface MinimumPriceRefreshUseCase {
    void refreshMinimumPriceInformation(final Long productId);
}
