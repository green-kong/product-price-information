package com.example.musinsaserver.product.application.service;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.product.application.port.in.RegisterProductUseCase;
import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;
import com.example.musinsaserver.product.application.port.out.event.ProductRegisterEventPublisher;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductRegisterEvent;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.application.port.out.validator.BrandValidator;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.product.exception.NonExistentBrandException;

@Service
public class RegisterProductService implements RegisterProductUseCase {

    private final ProductRepository productRepository;
    private final BrandValidator brandValidator;
    private final ProductRegisterEventPublisher publisher;

    public RegisterProductService(
            final ProductRepository productRepository,
            final BrandValidator brandValidator,
            final ProductRegisterEventPublisher publisher) {
        this.productRepository = productRepository;
        this.brandValidator = brandValidator;
        this.publisher = publisher;
    }

    @Override
    public Long registerProduct(final RegisterProductRequest registerProductRequest) {
        final Long brandId = registerProductRequest.brandId();
        validateBrandId(brandId);
        final Product product = registerProductRequest.toProduct();
        final Product savedProduct = productRepository.save(product);
        publisher.publishRegisterProductEvent(new ProductRegisterEvent(savedProduct.getId()));
        return savedProduct.getId();
    }

    private void validateBrandId(final Long brandId) {
        final boolean isValidBrandId = brandValidator.isExistedBrand(brandId);
        if(!isValidBrandId) {
            throw new NonExistentBrandException(brandId);
        }
    }
}
