package com.example.musinsaserver.product.application.port.out.event;

import com.example.musinsaserver.common.events.ProductDeleteEvent;

public interface ProductDeleteEventPublisher {
    void publishDeleteProductEvent(final ProductDeleteEvent productDeleteEvent);
}
