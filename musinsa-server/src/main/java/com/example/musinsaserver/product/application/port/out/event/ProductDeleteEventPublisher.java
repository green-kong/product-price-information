package com.example.musinsaserver.product.application.port.out.event;

import com.example.musinsaserver.product.application.port.out.event.dto.ProductDeleteEvent;

public interface ProductDeleteEventPublisher {
    void publishDeleteProductEvent(final ProductDeleteEvent productDeleteEvent);
}
