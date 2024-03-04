package com.example.musinsaserver.priceinformation.adaptor.in.event;

import org.springframework.context.event.EventListener;

import com.example.musinsaserver.priceinformation.application.port.in.MaximumPriceRefreshUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.MinimumPriceRefreshUseCase;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductRegisterEvent;

public class DeleteProductEventHandler {

    private final MinimumPriceRefreshUseCase minimumPriceRefreshUseCase;
    private final MaximumPriceRefreshUseCase maximumPriceRefreshUseCase;

    public DeleteProductEventHandler(
            final MinimumPriceRefreshUseCase minimumPriceRefreshUseCase,
            final MaximumPriceRefreshUseCase maximumPriceRefreshUseCase
    ) {
        this.minimumPriceRefreshUseCase = minimumPriceRefreshUseCase;
        this.maximumPriceRefreshUseCase = maximumPriceRefreshUseCase;
    }

    @EventListener
    public void updateMinimumPrice(final ProductRegisterEvent productRegisterEvent) {
        minimumPriceRefreshUseCase.refreshMinimumPriceInformation(productRegisterEvent.productId());
    }

    @EventListener
    public void updateMaximumPrice(final ProductRegisterEvent productRegisterEvent) {
        maximumPriceRefreshUseCase.refreshMaximumPriceInformation(productRegisterEvent.productId());
    }
}
