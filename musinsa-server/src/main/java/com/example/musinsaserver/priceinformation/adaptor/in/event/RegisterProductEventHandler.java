package com.example.musinsaserver.priceinformation.adaptor.in.event;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.musinsaserver.priceinformation.application.port.in.MaximumPriceUpdateUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.MinimumPriceUpdateUseCase;
import com.example.musinsaserver.common.events.ProductRegisterEvent;

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

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Async
    public void updateMinimumPrice(final ProductRegisterEvent productRegisterEvent) {
        minimumPriceUpdateUseCase.updateMinimumPrice(productRegisterEvent.brandId(), productRegisterEvent.categoryId());
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Async
    public void updateMaximumPrice(final ProductRegisterEvent productRegisterEvent) {
        maximumPriceUpdateUseCase.updateMaximumPrice(productRegisterEvent.brandId(), productRegisterEvent.categoryId());
    }
}
