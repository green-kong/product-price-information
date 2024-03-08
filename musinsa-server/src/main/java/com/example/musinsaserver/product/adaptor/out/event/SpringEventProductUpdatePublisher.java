package com.example.musinsaserver.product.adaptor.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.musinsaserver.common.events.ProductUpdateEvent;
import com.example.musinsaserver.product.application.port.out.event.ProductUpdateEventPublisher;

@Component
public class SpringEventProductUpdatePublisher implements ProductUpdateEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringEventProductUpdatePublisher(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publishUpdateProductEvent(final ProductUpdateEvent productUpdateEvent) {
        publisher.publishEvent(productUpdateEvent);
    }
}
