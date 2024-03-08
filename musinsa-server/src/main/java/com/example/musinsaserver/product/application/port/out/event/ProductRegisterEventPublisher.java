package com.example.musinsaserver.product.application.port.out.event;

import com.example.musinsaserver.common.events.ProductRegisterEvent;

public interface ProductRegisterEventPublisher {
    void publishRegisterProductEvent(final ProductRegisterEvent productRegisterEvent);

}
