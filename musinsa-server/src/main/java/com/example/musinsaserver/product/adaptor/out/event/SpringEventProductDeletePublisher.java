package com.example.musinsaserver.product.adaptor.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.musinsaserver.product.application.port.out.event.ProductDeleteEventPublisher;
import com.example.musinsaserver.common.events.ProductDeleteEvent;

@Component
public class SpringEventProductDeletePublisher implements ProductDeleteEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringEventProductDeletePublisher(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publishDeleteProductEvent(final ProductDeleteEvent productDeleteEvent) {
        publisher.publishEvent(productDeleteEvent);
    }
}
