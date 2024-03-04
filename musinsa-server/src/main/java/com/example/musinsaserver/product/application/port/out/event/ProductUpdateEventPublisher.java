package com.example.musinsaserver.product.application.port.out.event;

import com.example.musinsaserver.product.application.port.out.event.dto.ProductUpdateEvent;

public interface ProductUpdateEventPublisher {
    void publishUpdateProductEvent(final ProductUpdateEvent productUpdateEvent);
}
