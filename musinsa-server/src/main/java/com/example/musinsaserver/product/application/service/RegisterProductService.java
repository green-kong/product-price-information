package com.example.musinsaserver.product.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.product.application.port.in.RegisterProductUseCase;
import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;
import com.example.musinsaserver.product.application.port.out.event.ProductRegisterEventPublisher;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductRegisterEvent;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.application.port.out.validator.BrandValidator;
import com.example.musinsaserver.product.application.port.out.validator.CategoryValidator;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.product.exception.NonExistentCategoryException;
import com.example.musinsaserver.product.exception.NonExistentBrandException;

@Service
@Transactional(readOnly = true)
public class RegisterProductService implements RegisterProductUseCase {

    private final ProductRepository productRepository;
    private final BrandValidator brandValidator;
    private final CategoryValidator categoryValidator;
    private final ProductRegisterEventPublisher publisher;

    public RegisterProductService(
            final ProductRepository productRepository,
            final BrandValidator brandValidator,
            final CategoryValidator categoryValidator,
            final ProductRegisterEventPublisher publisher
    ) {
        this.productRepository = productRepository;
        this.brandValidator = brandValidator;
        this.categoryValidator = categoryValidator;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public Long registerProduct(final RegisterProductRequest registerProductRequest) {
        final Long brandId = registerProductRequest.brandId();
        final Long categoryId = registerProductRequest.categoryId();
        validateBrandId(brandId);
        validateCategoryId(categoryId);
        final Product product = registerProductRequest.toProduct();
        final Product savedProduct = productRepository.save(product);
        publisher.publishRegisterProductEvent(new ProductRegisterEvent(savedProduct.getId()));
        return savedProduct.getId();
    }

    private void validateCategoryId(final Long categoryId) {
        final boolean isValidCategoryId = categoryValidator.isExistedCategory(categoryId);
        if (!isValidCategoryId) {
            throw new NonExistentCategoryException(categoryId);
        }
    }

    private void validateBrandId(final Long brandId) {
        final boolean isValidBrandId = brandValidator.isExistedBrand(brandId);
        if(!isValidBrandId) {
            throw new NonExistentBrandException(brandId);
        }
    }
}
