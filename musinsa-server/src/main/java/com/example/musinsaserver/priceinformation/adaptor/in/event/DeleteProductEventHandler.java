package com.example.musinsaserver.priceinformation.adaptor.in.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.musinsaserver.priceinformation.application.port.in.MaximumPriceRefreshUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.MinimumPriceRefreshUseCase;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductDeleteEvent;

@Component
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
    public void updateMinimumPrice(final ProductDeleteEvent productDeleteEvent) {
        minimumPriceRefreshUseCase.refreshMinimumPriceInformation(productDeleteEvent.productId());
    }

    @EventListener
    public void updateMaximumPrice(final ProductDeleteEvent productDeleteEvent) {
        maximumPriceRefreshUseCase.refreshMaximumPriceInformation(productDeleteEvent.productId());
    }
}
