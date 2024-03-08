package com.example.musinsaserver.priceinformation.adaptor.in.event;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.musinsaserver.priceinformation.application.port.in.MaximumPriceRefreshUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.MinimumPriceRefreshUseCase;
import com.example.musinsaserver.common.events.ProductDeleteEvent;

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

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Async
    public void updateMinimumPrice(final ProductDeleteEvent productDeleteEvent) {
        minimumPriceRefreshUseCase.refreshMinimumPriceInformation(productDeleteEvent.productId());
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Async
    public void updateMaximumPrice(final ProductDeleteEvent productDeleteEvent) {
        maximumPriceRefreshUseCase.refreshMaximumPriceInformation(productDeleteEvent.productId());
    }
}
