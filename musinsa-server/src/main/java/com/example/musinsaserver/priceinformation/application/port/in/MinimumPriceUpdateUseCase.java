package com.example.musinsaserver.priceinformation.application.port.in;

import com.example.musinsaserver.product.application.port.out.event.dto.ProductRegisterEvent;

public interface MinimumPriceUpdateUseCase {
    void updateMinimumPriceUpdate(final ProductRegisterEvent productRegisterEvent);
}
