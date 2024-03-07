package com.example.musinsaserver.priceinformation.adaptor.out.loader;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;
import com.example.musinsaserver.priceinformation.application.port.out.loader.BrandLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.BrandLoadDto;

@Component
public class RepositoryBrandLoader implements BrandLoader {

    private final BrandRepository brandRepository;

    public RepositoryBrandLoader(final BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Optional<BrandLoadDto> loadBrand(final Long brandId) {
        final Brand brand = brandRepository.findById(brandId)
                .orElse(null);
        if (isNull(brand)) {
            return Optional.empty();
        }
        return Optional.of(new BrandLoadDto(brand.getId(), brand.getNameValue()));
    }

    @Override
    public List<BrandLoadDto> loadBrandByIds(final List<Long> brandIds) {
        final List<Brand> brands = brandRepository.findByIds(brandIds);
        return brands.stream()
                .map(brand -> new BrandLoadDto(brand.getId(), brand.getNameValue()))
                .toList();
    }
}
