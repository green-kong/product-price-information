package com.example.musinsaserver.priceinformation.application.port.out.persistence;

import java.util.Optional;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;

public interface PriceInformationRepository {
    PriceInformation save(final PriceInformation priceInformation);

    Optional<PriceInformation> findById(final Long id);

    Optional<PriceInformation> findByBrandIdAndCategory(final Long brandId, final String category);

    void updateById(final Long id, final PriceInformation minimumPriceInformation);

    Optional<PriceInformation> findByProductId(final Long productId);

    void deleteById(final Long id);
}
