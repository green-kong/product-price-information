package com.example.musinsaserver.product.adaptor.out.persistence.jpa;

import static java.util.Objects.isNull;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Product;

@Component
public class JpaProductRepository implements ProductRepository {

    private final JpaProductAdaptor products;
    private final JpaProductMapper mapper;

    public JpaProductRepository(
            final JpaProductAdaptor products,
            final JpaProductMapper mapper
    ) {
        this.products = products;
        this.mapper = mapper;
    }

    @Override
    public Product save(final Product product) {
        final JpaProductEntity jpaProductEntity = mapper.toJpaProductEntity(product);
        products.save(jpaProductEntity);
        return mapper.toProductDomainEntity(jpaProductEntity);
    }

    @Override
    public Optional<Product> findById(final Long id) {
        final JpaProductEntity jpaProductEntity = products.findById(id)
                .orElse(null);
        if (isNull(jpaProductEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toProductDomainEntity(jpaProductEntity));
    }

    @Override
    public void update(final Product product) {
        final JpaProductEntity jpaProductEntity = mapper.toJpaProductEntity(product);
        products.save(jpaProductEntity);
    }

    @Override
    public void delete(final Long id) {
        products.deleteById(id);
    }

    @Override
    public Optional<Product> findLowestPriceProductByBrandIdAndCategory(final Long brandId, final Long categoryId) {
        final JpaProductEntity jpaProductEntity = products.findFirstByBrandIdAndCategoryIdOrderByPriceAsc(brandId,
                        categoryId)
                .orElse(null);
        if (Objects.isNull(jpaProductEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toProductDomainEntity(jpaProductEntity));
    }

    @Override
    public Optional<Product> findHighestPriceProductByBrandIdAndCategory(final Long brandId, final Long categoryId) {
        final JpaProductEntity jpaProductEntity = products.findFirstByBrandIdAndCategoryIdOrderByPriceDesc(brandId,
                        categoryId)
                .orElse(null);
        if (Objects.isNull(jpaProductEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toProductDomainEntity(jpaProductEntity));
    }
}
