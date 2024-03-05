package com.example.musinsaserver.priceinformation.adaptor.out.persistence.jpa.highest;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaHighestPriceInformationAdaptor extends JpaRepository<JpaHighestPriceInformationEntity, Long> {
    Optional<JpaHighestPriceInformationEntity> findJpaHighestPriceInformationEntityByBrandIdAndCategoryId(
            final Long brandId, final Long categoryId);

    Optional<JpaHighestPriceInformationEntity> findJpaHighestPriceInformationEntityByProductId(final Long productId);
}
