package com.example.musinsaserver.priceinformation.application.port.in;

public interface MaximumPriceRefreshUseCase {
    void refreshMaximumPriceInformation(final Long productId);
}
