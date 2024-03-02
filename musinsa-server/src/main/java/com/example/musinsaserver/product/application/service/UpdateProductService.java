package com.example.musinsaserver.product.application.service;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.product.application.port.in.UpdateProductUseCase;
import com.example.musinsaserver.product.application.port.in.dto.ProductUpdateRequest;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.application.port.out.validator.BrandValidator;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.product.exception.NonExistentBrandException;
import com.example.musinsaserver.product.exception.NonExistentProductException;

@Service
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepository productRepository;
    private final BrandValidator brandValidator;

    public UpdateProductService(final ProductRepository productRepository, final BrandValidator brandValidator) {
        this.productRepository = productRepository;
        this.brandValidator = brandValidator;
    }

    @Override
    public void updateProduct(final Long productId, final ProductUpdateRequest productUpdateRequest) {
        final Long brandId = productUpdateRequest.brandId();
        validateBrandId(brandId);
        final Product product = getProductOrThrow(productId);
        product.update(productUpdateRequest.price(), productUpdateRequest.category(), brandId);
        productRepository.update(product);
    }

    private Product getProductOrThrow(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NonExistentProductException(productId));
    }

    private void validateBrandId(final Long brandId) {
        final boolean isValidBrandId = brandValidator.isExistedBrand(brandId);
        if (!isValidBrandId) {
            throw new NonExistentBrandException(brandId);
        }
    }
}
