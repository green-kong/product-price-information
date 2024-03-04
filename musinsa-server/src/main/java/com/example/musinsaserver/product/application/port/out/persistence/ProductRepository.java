package com.example.musinsaserver.product.application.port.out.persistence;

import java.util.Optional;

import com.example.musinsaserver.product.domain.Product;

public interface ProductRepository {
    Product save(final Product product);

    Optional<Product> findById(final Long id);

    void update(final Product product);

    void delete(final Long id);

    Optional<Product> findMinimumPriceProductByBrandIdAndCategory(final Long brandId, final String category);
}
