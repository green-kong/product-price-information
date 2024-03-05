package com.example.musinsaserver.product.adaptor.out.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductAdaptor extends JpaRepository<JpaProductEntity, Long> {

    Optional<JpaProductEntity> findFirstByBrandIdAndCategoryIdOrderByPriceAsc(Long brandId, Long categoryId);

    Optional<JpaProductEntity> findFirstByBrandIdAndCategoryIdOrderByPriceDesc(Long brandId, Long categoryId);
}
