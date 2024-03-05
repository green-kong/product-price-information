package com.example.musinsaserver.brand.adaptor.out.persistence.jpa;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;

@Component
public class JpaBrandRepository implements BrandRepository {

    private final JpaBrandAdaptor brands;
    private final JpaBrandMapper mapper;

    public JpaBrandRepository(final JpaBrandAdaptor brands, final JpaBrandMapper mapper) {
        this.brands = brands;
        this.mapper = mapper;
    }

    @Override
    public Brand save(final Brand brand) {
        final JpaBrandEntity jpaBrandEntity = mapper.toJpaBrandEntity(brand);
        brands.save(jpaBrandEntity);
        return mapper.toBrandDomainEntity(jpaBrandEntity);
    }

    @Override
    public Optional<Brand> findById(final Long id) {
        final JpaBrandEntity jpaBrandEntity = brands.findById(id)
                .orElse(null);
        if (Objects.isNull(jpaBrandEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toBrandDomainEntity(jpaBrandEntity));
    }

    @Override
    public Optional<Brand> findByName(final String name) {
        final JpaBrandEntity jpaBrandEntity = brands.findJpaBrandEntityByName(name)
                .orElse(null);
        if (Objects.isNull(jpaBrandEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toBrandDomainEntity(jpaBrandEntity));
    }
}
