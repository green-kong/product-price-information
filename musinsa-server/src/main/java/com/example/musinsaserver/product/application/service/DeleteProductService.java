package com.example.musinsaserver.product.application.service;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.product.application.port.in.DeleteProductUseCase;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.exception.NonExistentProductException;

@Service
public class DeleteProductService implements DeleteProductUseCase {

    private final ProductRepository productRepository;

    public DeleteProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void delete(final Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new NonExistentProductException(id));
        productRepository.delete(id);
    }
}
