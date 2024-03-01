package com.example.musinsaserver.product.application.service;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.product.application.port.in.RegisterProductUseCase;
import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Product;

@Service
public class RegisterProductService implements RegisterProductUseCase {

    private final ProductRepository productRepository;

    public RegisterProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Long registerProduct(final RegisterProductRequest registerProductRequest) {
        final Product product = registerProductRequest.toProduct();
        final Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }
}
