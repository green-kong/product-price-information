package com.example.musinsaserver.product.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.common.events.ProductUpdateEvent;
import com.example.musinsaserver.product.application.port.in.UpdateProductUseCase;
import com.example.musinsaserver.product.application.port.in.dto.ProductPriceUpdateRequest;
import com.example.musinsaserver.product.application.port.out.event.ProductUpdateEventPublisher;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.product.exception.NonExistentProductException;

@Service
@Transactional
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepository productRepository;
    private final ProductUpdateEventPublisher publisher;

    public UpdateProductService(
            final ProductRepository productRepository,
            final ProductUpdateEventPublisher publisher
    ) {
        this.productRepository = productRepository;
        this.publisher = publisher;
    }

    @Override
    public void updateProductPrice(final Long productId, final ProductPriceUpdateRequest productPriceUpdateRequest) {
        final Product product = getProductOrThrow(productId);
        product.update(productPriceUpdateRequest.price());
        productRepository.update(product);
        publisher.publishUpdateProductEvent(new ProductUpdateEvent(product.getBrandId(), product.getCategoryId()));
    }

    private Product getProductOrThrow(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NonExistentProductException(productId));
    }
}
