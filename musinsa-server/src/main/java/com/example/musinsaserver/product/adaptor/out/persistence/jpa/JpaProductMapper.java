package com.example.musinsaserver.product.adaptor.out.persistence.jpa;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.product.domain.Product;

@Component
public class JpaProductMapper {

    public JpaProductEntity toJpaProductEntity(final Product product) {
        return new JpaProductEntity(
                product.getId(),
                product.getCategoryId(),
                product.getPriceValue(),
                product.getBrandId()
        );
    }

    public Product toProductDomainEntity(final JpaProductEntity jpaProductEntity) {
        return Product.createWithId(
                jpaProductEntity.getId(),
                jpaProductEntity.getPrice(),
                jpaProductEntity.getCategoryId(),
                jpaProductEntity.getBrandId()
        );
    }
}
