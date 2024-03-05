package com.example.musinsaserver.priceinformation.adaptor.out.persistence.jpa.lowest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLowestPriceInformationAdaptor extends JpaRepository<JpaLowestPriceInformationEntity, Long> {
    Optional<JpaLowestPriceInformationEntity> findJpaLowestPriceInformationEntityByBrandIdAndCategoryId(
            final Long brandId, final Long categoryId);

    Optional<JpaLowestPriceInformationEntity> findJpaLowestPriceInformationEntityByProductId(final Long productId);

    List<JpaLowestPriceInformationEntity> findJpaLowestPriceInformationEntitiesByBrandId(final Long brandId);
}
