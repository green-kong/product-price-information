package com.example.musinsaserver.product.adaptor.out.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;
import com.example.musinsaserver.product.application.port.out.validator.BrandValidator;

@Component
public class RepositoryBrandValidator implements BrandValidator {

    private final BrandRepository brandRepository;

    public RepositoryBrandValidator(final BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public boolean isExistedBrand(final Long brandId) {
        final Optional<Brand> brand = brandRepository.findById(brandId);
        return brand.isPresent();
    }
}
