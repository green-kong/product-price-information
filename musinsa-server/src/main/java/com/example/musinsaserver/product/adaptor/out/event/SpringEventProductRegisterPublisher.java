package com.example.musinsaserver.product.adaptor.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.musinsaserver.product.application.port.out.event.ProductRegisterEventPublisher;
import com.example.musinsaserver.common.events.ProductRegisterEvent;

@Component
public class SpringEventProductRegisterPublisher implements ProductRegisterEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringEventProductRegisterPublisher(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publishRegisterProductEvent(final ProductRegisterEvent productRegisterEvent) {
        publisher.publishEvent(productRegisterEvent);
    }
}
