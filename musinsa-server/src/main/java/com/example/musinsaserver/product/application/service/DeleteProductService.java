package com.example.musinsaserver.product.application.service;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.product.application.port.in.DeleteProductUseCase;
import com.example.musinsaserver.product.application.port.out.event.ProductDeleteEventPublisher;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductDeleteEvent;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.product.exception.NonExistentProductException;

@Service
public class DeleteProductService implements DeleteProductUseCase {

    private final ProductRepository productRepository;
    private final ProductDeleteEventPublisher publisher;

    public DeleteProductService(
            final ProductRepository productRepository,
            final ProductDeleteEventPublisher publisher
    ) {
        this.productRepository = productRepository;
        this.publisher = publisher;
    }

    @Override
    public void delete(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new NonExistentProductException(id));
        productRepository.delete(product.getId());
        publisher.publishDeleteProductEvent(new ProductDeleteEvent(product.getId()));
    }
}
