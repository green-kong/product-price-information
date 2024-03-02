package com.example.musinsaserver.product.application.port.out.persistence;

import java.util.Optional;

import com.example.musinsaserver.product.domain.Product;

public interface ProductRepository {
    Product save(final Product product);

    Optional<Product> findById(final Long id);

    void update(Product product);
}
