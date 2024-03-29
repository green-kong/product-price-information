package com.example.musinsaserver.product.application.port.out.persistence;

import java.util.List;
import java.util.Optional;

import com.example.musinsaserver.product.domain.Product;

public interface ProductRepository {
    Product save(final Product product);

    Optional<Product> findById(final Long id);

    void update(final Product product);

    void delete(final Long id);

    Optional<Product> findLowestPriceProductByBrandIdAndCategory(final Long brandId, final Long categoryId);

    Optional<Product> findHighestPriceProductByBrandIdAndCategory(final Long brandId, final Long categoryId);

    List<Product> findAll();
}
