package com.example.musinsaserver.brand.application.port.out.persistence;

import java.util.List;
import java.util.Optional;

import com.example.musinsaserver.brand.domain.Brand;

public interface BrandRepository {
    Brand save(final Brand brand);

    List<Brand> findAll();

    Optional<Brand> findById(final Long id);

    Optional<Brand> findByName(final String name);
}
