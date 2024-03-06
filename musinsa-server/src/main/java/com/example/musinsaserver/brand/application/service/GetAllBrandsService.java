package com.example.musinsaserver.brand.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.brand.application.port.in.GetAllBrandsUseCase;
import com.example.musinsaserver.brand.application.port.in.dto.BrandResponse;
import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;

@Service
@Transactional(readOnly = true)
public class GetAllBrandsService implements GetAllBrandsUseCase {

    private final BrandRepository brandRepository;

    public GetAllBrandsService(final BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<BrandResponse> getAllBrands() {
        final List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(BrandResponse::from)
                .toList();
    }
}
