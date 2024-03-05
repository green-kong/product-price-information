package com.example.musinsaserver.brand.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.brand.application.port.in.RegisterBrandUseCase;
import com.example.musinsaserver.brand.application.port.in.dto.RegisterBrandRequest;
import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;
import com.example.musinsaserver.brand.exception.DuplicatedBrandNameException;

@Service
@Transactional(readOnly = true)
public class RegisterBrandService implements RegisterBrandUseCase {

    private final BrandRepository brandRepository;

    public RegisterBrandService(final BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    @Transactional
    public Long registerBrand(final RegisterBrandRequest registerBrandRequest) {
        final Brand brand = registerBrandRequest.toBrand();
        brandRepository.findByName(brand.getNameValue())
                .ifPresent(found->{
                    throw new DuplicatedBrandNameException(brand.getNameValue());});
        final Brand saveBrand = brandRepository.save(brand);
        return saveBrand.getId();
    }
}
