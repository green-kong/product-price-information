package com.example.musinsaserver.brand.application.service;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.brand.application.port.in.RegisterBrandUseCase;
import com.example.musinsaserver.brand.application.port.in.dto.RegisterBrandRequest;
import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;

@Service
public class RegisterBrandService implements RegisterBrandUseCase {

    private final BrandRepository brandRepository;

    public RegisterBrandService(final BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Long registerBrand(final RegisterBrandRequest registerBrandRequest) {
        final Brand brand = registerBrandRequest.toBrand();
        final Brand saveBrand = brandRepository.save(brand);
        return saveBrand.getId();
    }
}
