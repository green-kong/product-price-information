package com.example.musinsaserver.priceinformation.adaptor.in.event;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.musinsaserver.priceinformation.application.port.in.MaximumPriceUpdateUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.MinimumPriceUpdateUseCase;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductUpdateEvent;

@Component
public class UpdateProductEventHandler {
    private final MaximumPriceUpdateUseCase maximumPriceUpdateUseCase;
    private final MinimumPriceUpdateUseCase minimumPriceUpdateUseCase;

    public UpdateProductEventHandler(
            final MaximumPriceUpdateUseCase maximumPriceUpdateUseCase,
            final MinimumPriceUpdateUseCase minimumPriceUpdateUseCase
    ) {
        this.maximumPriceUpdateUseCase = maximumPriceUpdateUseCase;
        this.minimumPriceUpdateUseCase = minimumPriceUpdateUseCase;
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Async
    public void updateMinimumPrice(final ProductUpdateEvent productUpdateEvent) {
        minimumPriceUpdateUseCase.updateMinimumPrice(productUpdateEvent.brandId(), productUpdateEvent.categoryId());
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Async
    public void updateMaximumPrice(final ProductUpdateEvent productUpdateEvent) {
        maximumPriceUpdateUseCase.updateMaximumPrice(productUpdateEvent.brandId(), productUpdateEvent.categoryId());
    }
}
