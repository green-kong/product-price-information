package com.example.musinsaserver.priceinformation.adaptor.in.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.musinsaserver.priceinformation.application.port.in.MaximumPriceUpdateUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.MinimumPriceUpdateUseCase;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductRegisterEvent;

@Component
public class RegisterProductEventHandler {

    private final MinimumPriceUpdateUseCase minimumPriceUpdateUseCase;
    private final MaximumPriceUpdateUseCase maximumPriceUpdateUseCase;

    public RegisterProductEventHandler(
            final MinimumPriceUpdateUseCase minimumPriceUpdateUseCase,
            final MaximumPriceUpdateUseCase maximumPriceUpdateUseCase
    ) {
        this.minimumPriceUpdateUseCase = minimumPriceUpdateUseCase;
        this.maximumPriceUpdateUseCase = maximumPriceUpdateUseCase;
    }

    @EventListener
    public void updateMinimumPrice(final ProductRegisterEvent productRegisterEvent) {
        minimumPriceUpdateUseCase.updateMinimumPrice(productRegisterEvent.brandId(), productRegisterEvent.categoryId());
    }

    @EventListener
    public void updateMaximumPrice(final ProductRegisterEvent productRegisterEvent) {
        maximumPriceUpdateUseCase.updateMaximumPrice(productRegisterEvent.brandId(), productRegisterEvent.categoryId());
    }
}
