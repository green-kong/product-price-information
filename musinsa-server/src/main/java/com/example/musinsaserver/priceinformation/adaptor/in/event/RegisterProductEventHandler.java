package com.example.musinsaserver.priceinformation.adaptor.in.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.musinsaserver.priceinformation.application.port.in.MinimumPriceUpdateUseCase;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductRegisterEvent;

@Component
public class RegisterProductEventHandler {

    private final MinimumPriceUpdateUseCase minimumPriceUpdateUseCase;

    public RegisterProductEventHandler(final MinimumPriceUpdateUseCase minimumPriceUpdateUseCase) {
        this.minimumPriceUpdateUseCase = minimumPriceUpdateUseCase;
    }

    @EventListener
    public void updateMinimumPriceUpdate(final ProductRegisterEvent productRegisterEvent) {
        minimumPriceUpdateUseCase.updateMinimumPriceUpdate(productRegisterEvent);
    }
}
