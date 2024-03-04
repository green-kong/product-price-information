package com.example.musinsaserver.product.adaptor.out.persistence;

import static java.util.Objects.isNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Product;

@Component
public class InMemoryProductRepository implements ProductRepository {
    private Long insertId = 0L;
    private final Map<Long, Product> products = new HashMap<>();

    @Override
    public Product save(final Product product) {
        insertId += 1;
        final Product productWithId = Product.createWithId(
                insertId,
                product.getPriceValue(),
                product.getCategory(),
                product.getBrandId()
        );
        products.put(insertId, productWithId);
        return productWithId;
    }

    @Override
    public Optional<Product> findById(final Long id) {
        final Product product = products.get(id);
        if (isNull(product)) {
            return Optional.empty();
        }
        return Optional.of(product);
    }

    @Override
    public void update(final Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public void delete(final Long id) {
        products.remove(id);
    }

    @Override
    public Optional<Product> findMinimumPriceProductByBrandIdAndCategory(final Long brandId, final String category) {
        return products.values()
                .stream()
                .filter(product -> product.getBrandId().equals(brandId)
                        && product.getCategory().getValue().equals(category))
                .min(Comparator.comparingInt(Product::getPriceValue));
    }

    @Override
    public Optional<Product> findMaximumPriceProductByBrandIdAndCategory(final Long brandId, final String category) {
        return products.values()
                .stream()
                .filter(product -> product.getBrandId().equals(brandId)
                        && product.getCategory().getValue().equals(category))
                .max(Comparator.comparingInt(Product::getPriceValue));
    }
}
