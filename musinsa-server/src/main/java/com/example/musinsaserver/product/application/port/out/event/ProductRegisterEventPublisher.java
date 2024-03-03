package com.example.musinsaserver.product.application.port.out.event;

import com.example.musinsaserver.product.application.port.out.event.dto.ProductRegisterEvent;

public interface ProductRegisterEventPublisher {
    void publishRegisterProductEvent(final ProductRegisterEvent productRegisterEvent);

}
