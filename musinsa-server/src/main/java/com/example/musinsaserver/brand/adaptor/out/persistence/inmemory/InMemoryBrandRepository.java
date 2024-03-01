package com.example.musinsaserver.brand.adaptor.out.persistence.inmemory;

import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;

@Component
public class InMemoryBrandRepository implements BrandRepository {

    private long insertId = 0L;
    private final Map<Long, Brand> brands = new HashMap<>();

    @Override
    public Brand save(final Brand brand) {
        insertId += 1L;
        final Brand brandWithId = Brand.createWithId(insertId, brand.getNameValue());
        brands.put(insertId, brandWithId);
        return brandWithId;
    }

    @Override
    public Optional<Brand> findById(final Long id) {
        final Brand brand = brands.get(id);
        if (isNull(brand)) {
            return Optional.empty();
        }
        return Optional.of(brand);
    }
}
