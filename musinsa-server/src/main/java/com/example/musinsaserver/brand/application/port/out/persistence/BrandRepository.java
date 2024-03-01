package com.example.musinsaserver.brand.application.port.out.persistence;

import java.util.Optional;

import com.example.musinsaserver.brand.domain.Brand;

public interface BrandRepository {
    Brand save(final Brand brand);

    Optional<Brand> findById(final Long id);
}
